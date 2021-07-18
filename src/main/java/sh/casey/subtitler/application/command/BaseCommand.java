package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.application.exception.InvalidCommandException;
import sh.casey.subtitler.model.SubtitleType;

import java.util.Arrays;

@Slf4j
abstract class BaseCommand implements ApplicationCommand {

    protected final CommandLine cmd;
    private String inputFilename;
    private String outputFilename;

    public BaseCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    protected String getInputFilename() {
        if (StringUtils.isNotBlank(inputFilename)) {
            return inputFilename;
        }

        String inputFilename;
        if (cmd.hasOption('i')) {
            inputFilename = cmd.getOptionValue('i');
        } else if (!cmd.getArgList().isEmpty()) {
            inputFilename = cmd.getArgList().get(0);
        } else {
            throw new InvalidCommandException("Input filename is required.");
        }
        log.debug("Using input file " + inputFilename);
        this.inputFilename = inputFilename;
        return inputFilename;
    }

    protected SubtitleType getInputFileType() {
        if (cmd.hasOption("it")) {
            try {
                return SubtitleType.find(cmd.getOptionValue("it"));
            } catch (IllegalArgumentException e) {
                throw new InvalidCommandException("Could not find subtitle type '" + cmd.getOptionValue("it") + "'. Valid options are " + Arrays.toString(SubtitleType.values()));
            }
        }

        log.debug("Attempting to resolve input type based on file extension...");
        String inputFilename = getInputFilename();
        String[] parts = inputFilename.split("\\.");
        String extension = parts[parts.length - 1];
        try {
            SubtitleType type = SubtitleType.find(extension);
            log.debug("Resolved subtitle type to " + type.name());
            return type;
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Could not infer subtitle type from input file. Please use the -it <input-type> option or rename your subtitle extension to a standard format (.srt, .ass, etc).");
        }
    }

    protected SubtitleType getOutputFileType() {
        if (cmd.hasOption("ot")) {
            try {
                return SubtitleType.find(cmd.getOptionValue("ot"));
            } catch (IllegalArgumentException e) {
                throw new InvalidCommandException("Could not find subtitle type '" + cmd.getOptionValue("it") + "'. Valid options are " + Arrays.toString(SubtitleType.values()));
            }
        }

        log.debug("Attempting to resolve output type based on file extension...");
        String outputFilename = getOutputFilename();
        String[] parts = outputFilename.split("\\.");
        String extension = parts[parts.length - 1];
        try {
            SubtitleType type = SubtitleType.find(extension);
            log.debug("Resolved subtitle type to " + type.name());
            return type;
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Could not infer subtitle type from output file. Please use the -ot <output-type> option or rename your subtitle extension to a standard format (.srt, .ass, etc).");
        }
    }

    protected String getOutputFilename() {
        if (StringUtils.isNotBlank(outputFilename)) {
            return outputFilename;
        }

        String outputFilename;
        if (cmd.hasOption("o")) {
            outputFilename = cmd.getOptionValue('o');
        } else {
            outputFilename = getInputFilename();
        }
        log.debug("Using output file " + outputFilename);
        this.outputFilename = outputFilename;
        return outputFilename;
    }
}
