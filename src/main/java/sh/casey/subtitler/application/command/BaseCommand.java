package sh.casey.subtitler.application.command;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import sh.casey.subtitler.application.command.completer.FileCompleter;
import sh.casey.subtitler.application.command.completer.SubtitleTypeCompleter;
import sh.casey.subtitler.model.SubtitleType;

abstract class BaseCommand implements Runnable {

    @Spec
    CommandSpec spec;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display this help message.")
    private boolean help;

    @Option(names = {"-i", "--input"}, description = "The input file to read from.", required = true, completionCandidates = FileCompleter.class, paramLabel = "<file>")
    private String input;

    @Option(names = {"-I", "--input-type"}, description = "The type of subtitle file to read from. If not specified, the file extension will be used. Valid options: ${COMPLETION-CANDIDATES}", completionCandidates = SubtitleTypeCompleter.class, paramLabel = "<type>")
    private SubtitleType inputType;

    @Option(names = {"-o", "--output"}, description = "The output file to write to. If not specified, the input file will be used.", completionCandidates = FileCompleter.class, paramLabel = "<file>")
    private String output;

    @Option(names = {"-O", "--output-type"}, description = "The type of subtitle file to write to. If not specified, the input type will be used. Valid options: ${COMPLETION-CANDIDATES}", completionCandidates = SubtitleTypeCompleter.class, paramLabel = "<type>")
    private SubtitleType outputType;

    public String getInput() {
        if (input != null) {
            return input;
        }

        throw new ParameterException(spec.commandLine(), "Input file is required.");
    }

    protected String getOutput() {
        if (output != null) {
            return output;
        }

        return input;
    }

    protected SubtitleType getOutputType() {
        if (outputType != null) {
            return outputType;
        }

        if (output != null) {
            return getTypeFromFilename(output);
        }

        return getInputType();
    }

    protected SubtitleType getInputType() {
        if (inputType != null) {
            return inputType;
        }
        return getTypeFromFilename(input);
    }

    private SubtitleType getTypeFromFilename(String filename) {
        if (filename == null) {
            return null;
        }

        final String[] parts = filename.split("\\.");
        final String extension = parts[parts.length - 1];
        return SubtitleType.find(extension);
    }

}
