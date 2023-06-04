package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.exception.CommandNotFoundException;
import sh.casey.subtitler.filter.FilterType;

public class CommandFactory {

    public ApplicationCommand getInstance(final CommandLine cmd) {
        for (FilterType value : FilterType.values()) {
            if (cmd.hasOption(value.getLongOpt())) {
                return new FilterCommand(cmd);
            }
        }

        if (cmd.hasOption("v")) {
            return new VersionCommand();
        } else if (cmd.hasOption("cn")) {
            return new CondenseCommand(cmd);
//        } else if (cmd.hasOption("s")) {
//            return new ShiftCommand(cmd);
        } else if (cmd.hasOption("r")) {
            return new RenumberCommand(cmd);
        } else if (cmd.hasOption("c")) {
            return new ConvertCommand(cmd);
        } else if (cmd.hasOption("d")) {
            return new DualSubtitleCommand(cmd);
        } else if (cmd.hasOption("n")) {
            return new NormalizeCommand(cmd);
        } else if (cmd.hasOption("p")) {
            return new PrintCommand(cmd);
        } else if (cmd.hasOption("align")) {
            return new AlignCommand(cmd);
        } else {
            throw new CommandNotFoundException("Could not find command for given arguments");
        }
    }
}
