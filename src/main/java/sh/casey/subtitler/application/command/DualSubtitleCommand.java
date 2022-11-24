package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.DualSubCreator;
import sh.casey.subtitler.application.DualSubConfig;
import sh.casey.subtitler.application.exception.InvalidCommandException;

@Slf4j
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

        // Parse the dual subtitles configuration
        DualSubConfig.DualSubConfigBuilder config = DualSubConfig.builder();
        if (cmd.hasOption("dual-subs-config")) {
            final String cfg = cmd.getOptionValue("dual-subs-config");
            final String[] options = cfg.split(",");
            for (String option : options) {
                if (!option.contains("=")) {
                    log.warn("Invalid dual subs option {}", option);
                    continue;
                }
                String[] keyValue = option.split("=");
                if (keyValue.length != 2) {
                    log.warn("Invalid dual subs option: {}", option);
                }
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "font":
                        config.font(value);
                        break;
                    case "size":
                        config.size(value);
                        break;
                    case "bold":
                        config.bold(Boolean.parseBoolean(value));
                        break;
                    default:
                        log.warn("Invalid dual subs option key: {}", key);
                        break;
                }
            }
        }

        DualSubCreator.builder()
            .topFile(cmd.getOptionValue("tf"))
            .bottomFile(cmd.getOptionValue("bf"))
            .outputFile(cmd.getOptionValue("o"))
            .config(config.build())
            .build()
            .create();
    }
}
