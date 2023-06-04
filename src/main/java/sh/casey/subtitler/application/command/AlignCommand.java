package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
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
public class AlignCommand extends BaseCommand {

    private static final String REFERENCE_OPTION = "reference";
    // The default threshold to align subtitles at. Any subtitles that aren't aligned within
    // the following number of milliseconds will be aligned.
    private static final int DEFAULT_THRESHOLD = 500;

    public AlignCommand(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        final String inputFilename = getInputFilename();
        final SubtitleType inputType = getInputFileType();
        if (!cmd.hasOption(REFERENCE_OPTION)) {
            throw new InvalidCommandException("You must specify a reference file.");
        }

        final String referenceFilename = cmd.getOptionValue(REFERENCE_OPTION);
        final SubtitleType referenceType = getReferenceFileType();
        final SubtitleReaderFactory factory = new SubtitleReaderFactory();
        final SubtitleReader<SubtitleFile> inputReader = factory.getInstance(inputType);
        final SubtitleReader<SubtitleFile> referenceReader = factory.getInstance(referenceType);

        final SubtitleFile input = inputReader.read(inputFilename);
        final SubtitleFile reference = referenceReader.read(referenceFilename);
        if (inputType != referenceType) {
            throw new InvalidCommandException("Only input and reference files of the same type are currently supported for alignment. First, convert one of the subtitles to the format of the other.");
        }

        ShiftMode mode = ShiftMode.FROM_TO;
        if (cmd.hasOption("align-mode")) {
            mode = ShiftMode.findByString(cmd.getOptionValue("align-mode"));
        }

        int threshold = cmd.hasOption("threshold") ? Integer.parseInt(cmd.getOptionValue("threshold")) : DEFAULT_THRESHOLD;
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

        final String outputFilename = getOutputFilename();
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(getOutputFileType());
        writer.write(input, outputFilename);
    }

    private SubtitleType getReferenceFileType() {
        final String reference = cmd.getOptionValue(REFERENCE_OPTION);
        final String[] parts = reference.split("\\.");
        final String extension = parts[parts.length - 1];
        return SubtitleType.find(extension);
    }
}
