package sh.casey.subtitler.application.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.shifter.ShiftConfig;
import sh.casey.subtitler.shifter.ShiftMode;
import sh.casey.subtitler.shifter.SubtitleShifter;
import sh.casey.subtitler.shifter.SubtitleShifterFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

@Command(name = "shift", description = "Shifts the subtitles by the specified amount of time.", mixinStandardHelpOptions = true)
public class ShiftCommand extends BasePicocliCommand {

    @Option(names = {"-t", "--time"}, description = "The amount of time to shift the subtitles in milliseconds.", required = true)
    private String time;

    @Option(names = {"-b", "--before"}, description = "Only shift subtitles in the file before this time (format HH:mm:ss,SSS).", paramLabel = "<time>")
    private String before;

    @Option(names = {"-a", "--after"}, description = "Only shift subtitles in the file after this time (format HH:mm:ss,SSS).", paramLabel = "<time>")
    private String after;

    @Option(names = {"-n", "--number"}, description = "The number of a specific subtitle to shift.")
    private Integer number;

    @Option(names = {"-c", "--contains"}, description = "The text (or part of the text) of subtitle to shift.", paramLabel = "<text>")
    private String contains;

    @Option(names = {"-m", "--mode"}, description = "The mode to use when shifting the subtitles. Valid values are: ${COMPLETION-CANDIDATES}.", completionCandidates = ShiftModeCompleter.class, defaultValue = "FROM_TO", required = true)
    private ShiftMode mode;

    @Override
    public void run() {
        String input = getInput();
        SubtitleType inputType = getInputType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(inputType);
        final SubtitleShifter<SubtitleFile> shifter = new SubtitleShifterFactory().getInstance(inputType);
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(getOutputType());
        final SubtitleFile file = reader.read(input);

        final ShiftConfig config = ShiftConfig.builder()
            .ms(Integer.parseInt(time))
            .before(before)
            .after(after)
            .number(number)
            .matches(contains)
            .shiftMode(mode)
            .build();
        shifter.shift(file, config);
        writer.write(file, getOutput());
    }

    static class ShiftModeCompleter implements Iterable<String> {

        @Override
        public Iterator<String> iterator() {
            return Arrays.stream(ShiftMode.values())
                .map(ShiftMode::toString)
                .collect(Collectors.toList())
                .iterator();
        }
    }
}
