package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.application.exception.InvalidCommandException;
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

class ShiftCommand extends BaseCommand {

    public ShiftCommand(final CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        if (!cmd.hasOption('t')) {
            throw new InvalidCommandException("You must pass in the time to shift the subtitles in milliseconds.");
        }

        final String input = getInputFilename();
        final SubtitleType subtitleType = getInputFileType();
        final String output = getOutputFilename();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(subtitleType);
        final SubtitleShifter<SubtitleFile> shifter = new SubtitleShifterFactory().getInstance(subtitleType);
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(subtitleType);
        final SubtitleFile file = reader.read(input);
        final Integer number = cmd.hasOption('n') && StringUtils.isNumeric(cmd.getOptionValue('n')) ? Integer.parseInt(cmd.getOptionValue('n')) : null;

        final ShiftConfig config = ShiftConfig.builder()
            .ms(Integer.parseInt(cmd.getOptionValue('t')))
            .before(cmd.getOptionValue('b'))
            .after(cmd.getOptionValue('a'))
            .number(number)
            .matches(cmd.getOptionValue('m'))
            .shiftMode(ShiftMode.findByString(cmd.getOptionValue("sm")))
            .build();
        shifter.shift(file, config);
        writer.write(file, output);
    }
}
