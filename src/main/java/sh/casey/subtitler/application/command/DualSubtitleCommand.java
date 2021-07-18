package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.DualSubCreator;
import sh.casey.subtitler.application.exception.InvalidCommandException;

class DualSubtitleCommand extends BaseCommand {

    public DualSubtitleCommand(final CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        if (!cmd.hasOption("tf")) {
            throw new InvalidCommandException("You must specify a top file.");
        }

        if (!cmd.hasOption("bf")) {
            throw new InvalidCommandException("You must specify a bottom file.");
        }

        if (!cmd.hasOption("o")) {
            throw new InvalidCommandException("You must specify an output file.");
        }

        DualSubCreator.builder()
            .topFile(cmd.getOptionValue("tf"))
            .bottomFile(cmd.getOptionValue("bf"))
            .outputFile(cmd.getOptionValue("o"))
            .build()
            .create();
    }
}
