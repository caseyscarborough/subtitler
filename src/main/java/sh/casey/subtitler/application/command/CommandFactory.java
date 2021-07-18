package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.exception.CommandNotFoundException;

public class CommandFactory {

    public ApplicationCommand getInstance(CommandLine cmd) {
        if (cmd.hasOption("cn")) {
            return new CondenseCommand(cmd);
        } else if (cmd.hasOption("s")) {
            return new ShiftCommand(cmd);
        } else if (cmd.hasOption("r")) {
            return new RenumberCommand(cmd);
        } else if (cmd.hasOption("c")) {
            return new ConvertCommand(cmd);
        } else if (cmd.hasOption("d")) {
            return new DualSubtitleCommand(cmd);
        } else {
            throw new CommandNotFoundException("Could not find command for given arguments");
        }
    }
}
