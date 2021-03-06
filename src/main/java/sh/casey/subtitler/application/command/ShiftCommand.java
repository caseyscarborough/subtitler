package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.exception.InvalidCommandException;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.shifter.ShiftConfig;
import sh.casey.subtitler.shifter.ShiftMode;
import sh.casey.subtitler.shifter.SubtitleShifter;
import sh.casey.subtitler.shifter.SubtitleShifterFactory;

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
        final SubtitleShifter<? extends SubtitleFile> shifter = new SubtitleShifterFactory().getInstance(subtitleType);
        final Integer number = cmd.getOptionValue('n') != null ? Integer.parseInt(cmd.getOptionValue('n')) : null;

        final ShiftConfig config = ShiftConfig.builder()
            .input(input)
            .output(output)
            .ms(Integer.parseInt(cmd.getOptionValue('t')))
            .before(cmd.getOptionValue('b'))
            .after(cmd.getOptionValue('a'))
            .number(number)
            .matches(cmd.getOptionValue('m'))
            .shiftMode(ShiftMode.findByString(cmd.getOptionValue("sm")))
            .build();
        shifter.shift(config);
    }
}
