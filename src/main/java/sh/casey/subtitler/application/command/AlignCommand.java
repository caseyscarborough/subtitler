package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import sh.casey.subtitler.application.command.completer.FileCompleter;
import sh.casey.subtitler.application.command.completer.ShiftModeCompleter;
import sh.casey.subtitler.application.exception.InvalidCommandException;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.shifter.ShiftMode;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

@Slf4j
@Command(name = "align", description = "Align subtitles within a specific threshold to a reference file. This will adjust the start/end times of subtitles to closely match the reference file.")
public class AlignCommand extends BasePicocliCommand {

    @Option(names = {"-t", "--threshold"}, description = "The threshold to align subtitles at in milliseconds. Any subtitles that aren't aligned within this threshold will be aligned.", paramLabel = "<threshold>", defaultValue = "500", required = true)
    private int threshold;

    @Option(names = {"-r", "--reference"}, description = "The reference file to align the subtitles to.", paramLabel = "<file>", required = true, completionCandidates = FileCompleter.class)
    private String referenceFilename;

    @Option(names = {"-m", "--mode"}, description = "The mode to use when aligning the subtitles. Valid values are: ${COMPLETION-CANDIDATES}.", completionCandidates = ShiftModeCompleter.class, defaultValue = "FROM_TO", required = true)
    private ShiftMode mode;

    @Override
    public void run() {
        final String inputFilename = getInput();
        final SubtitleType inputType = getInputType();

        final SubtitleType referenceType = getReferenceFileType();
        final SubtitleReaderFactory factory = new SubtitleReaderFactory();
        final SubtitleReader<SubtitleFile> inputReader = factory.getInstance(inputType);
        final SubtitleReader<SubtitleFile> referenceReader = factory.getInstance(referenceType);

        final SubtitleFile input = inputReader.read(inputFilename);
        final SubtitleFile reference = referenceReader.read(referenceFilename);
        if (inputType != referenceType) {
            throw new InvalidCommandException("Only input and reference files of the same type are currently supported for alignment. First, convert one of the subtitles to the format of the other.");
        }

        log.debug("Using threshold of {}ms...", threshold);

        int i = 0;
        int j = 0;
        int startTimes = 0;
        int endTimes = 0;
        log.info("Aligning subtitles in \"{}\" to \"{}\"...", inputFilename, referenceFilename);
        while (i < input.getSubtitles().size() && j < reference.getSubtitles().size()) {
            final Subtitle a = input.getSubtitles().get(i);
            final Subtitle b = reference.getSubtitles().get(j);
            final long startDiff = Math.abs(a.getStartMilliseconds() - b.getStartMilliseconds());
            final long endDiff = Math.abs(a.getEndMilliseconds() - b.getEndMilliseconds());

            if (startDiff != 0 && startDiff < threshold && (mode == ShiftMode.FROM_TO || mode == ShiftMode.FROM)) {
                a.setStart(b.getStart());
                startTimes++;
            }

            if (endDiff != 0 && endDiff < threshold && (mode == ShiftMode.FROM_TO || mode == ShiftMode.TO)) {
                a.setEnd(b.getEnd());
                endTimes++;
            }

            if (a.getStartMilliseconds() >= b.getEndMilliseconds()) {
                j++;
            } else {
                i++;
            }
        }

        log.info("Successfully aligned {} start time(s) and {} end time(s).", startTimes, endTimes);

        final String outputFilename = getOutput();
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(getOutputType());
        writer.write(input, outputFilename);
    }

    private SubtitleType getReferenceFileType() {
        final String[] parts = referenceFilename.split("\\.");
        final String extension = parts[parts.length - 1];
        return SubtitleType.find(extension);
    }
}
