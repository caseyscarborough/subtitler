package sh.casey.subtitler.cli.command;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import sh.casey.subtitler.cli.command.completer.FileCompleter;
import sh.casey.subtitler.cli.command.completer.FilterTypeCompleter;
import sh.casey.subtitler.cli.command.completer.ShiftModeCompleter;
import sh.casey.subtitler.filter.FilterType;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.shifter.ShiftMode;
import sh.casey.subtitler.util.TimeUtil;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Setter(AccessLevel.PACKAGE)
@Command(name = "align", aliases = "a", description = "Align subtitles within a specific threshold to a reference file. This will adjust the start/end times of subtitles to closely match the reference file.", sortOptions = false, sortSynopsis = false)
public class AlignCommand extends BaseCommand {

    private static final Integer MIN_SUBTITLE_LENGTH = 300;

    @Option(names = {"-t", "--threshold"}, description = "The threshold to align subtitles at in milliseconds. Any subtitles that aren't aligned within this threshold will be aligned.", paramLabel = "<threshold>", defaultValue = "500", required = true)
    private int threshold;

    @Option(names = {"-r", "--reference"}, description = "The reference file to align the subtitles to.", paramLabel = "<file>", required = true, completionCandidates = FileCompleter.class)
    private String referenceFilename;

    @Option(names = {"-m", "--mode"}, description = "The mode to use when aligning the subtitles. Valid values are: ${COMPLETION-CANDIDATES}.", completionCandidates = ShiftModeCompleter.class, defaultValue = "FROM_TO", required = true)
    private ShiftMode mode;

    @Option(names = {"-s", "--skip"}, description = "Skip subtitles matching a specific filter. Filters should be separated by a semicolon, and should be key-value pairs. Multiple values can be used by separating them with a comma, for example. --skip \"style=op,ed,signs,songs\". Valid filters are: ${COMPLETION-CANDIDATES}.", required = false, completionCandidates = FilterTypeCompleter.class)
    private String filters;

    @Option(names = {"-n", "--no-overlap"}, description = "Avoid overlapping subtitles when modifying start/end times.", required = false)
    private boolean noOverlap;

    @Override
    public void doRun() {
        final String inputFilename = getInput();
        final SubtitleType inputType = getInputType();

        final SubtitleType referenceType = getTypeFromFilename(referenceFilename);
        final SubtitleReaderFactory factory = new SubtitleReaderFactory();
        final SubtitleReader<SubtitleFile> inputReader = factory.getInstance(inputType);
        final SubtitleReader<SubtitleFile> referenceReader = factory.getInstance(referenceType);

        final SubtitleFile input = inputReader.read(inputFilename);
        final SubtitleFile reference = referenceReader.read(referenceFilename);
        if (inputType != referenceType) {
            throw new IllegalStateException("Only input and reference files of the same type are currently supported for alignment. First, convert one of the subtitles to the format of the other.");
        }

        int i = 0;
        int j = 0;

        log.info("Aligning subtitles in \"{}\" to \"{}\"...", inputFilename, referenceFilename);
        log.debug("Using threshold of {}ms...", threshold);

        input.getSubtitles().sort(Comparator.comparing(Subtitle::getStartMilliseconds));
        reference.getSubtitles().sort(Comparator.comparing(Subtitle::getStartMilliseconds));
        Map<Integer, Long> startModified = new HashMap<>();
        Map<Integer, Long> endModified = new HashMap<>();
        while (i < input.getSubtitles().size() && j < reference.getSubtitles().size()) {
            final Subtitle a = input.getSubtitles().get(i);
            final Subtitle b = reference.getSubtitles().get(j);
            final Subtitle previous = i > 0 ? input.getSubtitles().get(i - 1) : null;
            if (filters != null && a instanceof AssDialogue) {
                AssDialogue ass = (AssDialogue) a;
                boolean skipped = false;
                for (String filter : filters.split(";")) {
                    final String[] parts = filter.split("=");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Filter was an invalid format: " + filter);
                    }
                    final String key = parts[0].toLowerCase();
                    final List<String> values = Arrays.stream(parts[1].split(",")).map(String::toLowerCase).collect(Collectors.toList());
                    if (key.equals(FilterType.STYLE.getName().toLowerCase()) && values.contains(ass.getStyle().toLowerCase())) {
                        log.trace("Skipping dialogue at {} because it matches filter {}={}", ass.getStart(), key, ass.getStyle().toLowerCase());
                        i++;
                        skipped = true;
                        break;
                    }
                }
                if (skipped) {
                    continue;
                }
            }
            final long startDiff = Math.abs(a.getStartMilliseconds() - b.getStartMilliseconds());
            final long endDiff = Math.abs(a.getEndMilliseconds() - b.getEndMilliseconds());

            if (startDiff != 0 && startDiff <= threshold &&
                (mode == ShiftMode.FROM_TO || mode == ShiftMode.FROM) &&
                // ensure we don't lose the subtitle from making the start time after the end time
                (a.getEndMilliseconds() > b.getStartMilliseconds()) &&
                // don't let the length of the new subtitle be less than the threshold
                (a.getEndMilliseconds() - b.getStartMilliseconds() > MIN_SUBTITLE_LENGTH) &&
                // This check will prevent two subtitles from overlapping if the first subtitle length is shorter than the threshold
                !(previous != null && previous.getStartMilliseconds().equals(b.getStartMilliseconds()))) {
                log.trace("{} Setting subtitle '{}' from start {} to start {}.", a.getStart(), a.getText(), a.getStart(), b.getStart());
                startModified.put(a.getNumber(), a.getStartMilliseconds());
                a.setStart(b.getStart());
            }

            if (endDiff != 0 && endDiff <= threshold &&
                (mode == ShiftMode.FROM_TO || mode == ShiftMode.TO) &&
                // make sure we don't lose the subtitle from making the end time before the start time
                (a.getStartMilliseconds() < b.getEndMilliseconds()) &&
                // don't let the length of the new subtitle be less than the threshold
                (b.getEndMilliseconds() - a.getStartMilliseconds() > MIN_SUBTITLE_LENGTH)) {
                log.trace("{} Setting subtitle '{}' from end {} to end {}.", a.getStart(), a.getText(), a.getEnd(), b.getEnd());
                endModified.put(a.getNumber(), a.getEndMilliseconds());
                a.setEnd(b.getEnd());
            }

            if (startModified.containsKey(a.getNumber()) && endModified.containsKey(a.getNumber())) {
                // If the start time and the end time of the subtitle were both modified, then there's nothing
                // more to be done with this subtitle, and we move to the next one.
                i++;
            } else if (b.getEndMilliseconds() - threshold > a.getEndMilliseconds()) {
                // If the end of the reference subtitle is after the end of the input subtitle,
                // we will move to the next input subtitle.
                i++;
            } else if (a.getEndMilliseconds() - threshold > b.getEndMilliseconds()) {
                // If the end of the input subtitle is after the end of the reference subtitle,
                // we will move to the next reference subtitle.
                j++;
            } else {
                // Otherwise we will just move to the next reference subtitle.
                j++;
            }
        }

        if (noOverlap) {
            // Lastly, we will fix any newly overlapping subtitles after modification.
            resolveOverlaps(input, startModified, endModified);
        }

        log.info("Successfully aligned {} start time(s) and {} end time(s).", startModified.size(), endModified.size());

        final String outputFilename = getOutput();
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(getOutputType());
        writer.write(input, outputFilename);
    }

    private void resolveOverlaps(SubtitleFile input, Map<Integer, Long> startModified, Map<Integer, Long> endModified) {
        for (int i = 0; i < input.getSubtitles().size() - 1; i++) {
            final Subtitle current = input.getSubtitles().get(i);
            final Subtitle next = input.getSubtitles().get(i + 1);
            if (current.getEndMilliseconds() <= next.getStartMilliseconds()) {
                continue;
            }

            if (!endModified.containsKey(current.getNumber()) && !startModified.containsKey(next.getNumber())) {
                continue;
            }

            if (log.isTraceEnabled()) {
                log.warn("Subtitle at {} --> {} overlaps with subtitle at {} --> {} after modification.", current.getStart(), current.getEnd(), next.getStart(), next.getEnd());
            }
            long originalCurrentEnd = endModified.getOrDefault(current.getNumber(), current.getEndMilliseconds());
            long originalNextStart = startModified.getOrDefault(next.getNumber(), next.getStartMilliseconds());
            if (originalCurrentEnd > originalNextStart) {
                log.trace("Nothing to be done, this overlap existed in the original file.");
                continue;
            }

            final long overlap = current.getEndMilliseconds() - next.getStartMilliseconds();
            if (overlap > threshold) {
                log.trace("Overlap is greater than threshold, so we will not modify the subtitle.");
                continue;
            }

            // TODO: What to do if the subtitles overlap?
            // Options:
            // - Break the overlapped subtitle into two separate subtitles, one that fully overlaps and one that doesn't,
            //   so that the subtitle will revert to the correct position on screen after the overlap is done.
            // - Set the end time of the overlapped subtitle to the start time of the next subtitle.
            // - Set the start time of the next subtitle to the end time of the overlapped subtitle.
            // - Do nothing and let the user fix it manually.
            if (endModified.containsKey(current.getNumber()) && startModified.containsKey(next.getNumber())) {
                // the overlap here is likely significant, i.e. two dialogues happening at the same time, so we will do nothing.
                String end = TimeUtil.millisecondsToTime(current.getType(), originalCurrentEnd);
                String start = TimeUtil.millisecondsToTime(next.getType(), originalNextStart);
                log.trace("Resetting the end time of the first subtitle from {} to {}.", current.getEnd(), end);
                current.setEnd(end);
                log.trace("Resetting the start time of the second subtitle from {} to {}.", next.getStart(), start);
                next.setStart(start);
                // TODO: Alternative to resetting to the original times
                // We could also check which subtitle was shifted the least, and set the overlapping time of the other
                // subtitle to that time. This would allow us to keep the subtitles aligned with the reference file,
                // which will cause the end result to look smoother.
            } else if (endModified.containsKey(current.getNumber())) {
                log.trace("Fixing overlap by setting the end time of the first subtitle from {} to {}.", current.getEnd(), next.getStart());
                current.setEnd(next.getStart());
            } else if (startModified.containsKey(next.getNumber())) {
                log.trace("Fixing overlap by setting the start time of the second subtitle from {} to {}.", next.getStart(), current.getEnd());
                next.setStart(current.getEnd());
            } else {
                log.trace("Nothing to be done, this overlap existed in the original file and the subtitles were not modified.");
                // the subtitles were not modified, which means that this is likely the intended behavior from the original file
            }
        }
    }
}
