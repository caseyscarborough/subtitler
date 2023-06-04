package sh.casey.subtitler.application.command;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;
import sh.casey.subtitler.model.SubtitleType;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

abstract class BasePicocliCommand implements Runnable {

    @Option(names = {"-i", "--input"}, description = "The input file to read from.", required = true, completionCandidates = FileCompleter.class, paramLabel = "<file>")
    private String input;

    @Option(names = {"-o", "--output"}, description = "The output file to write to. If not specified, the input file will be used.", completionCandidates = FileCompleter.class, paramLabel = "<file>")
    private String output;

    @Option(names = {"--input-type"}, description = "The type of subtitle file to read from. If not specified, the file extension will be used. Valid options: ${COMPLETION-CANDIDATES}", completionCandidates = TypeCompleter.class, paramLabel = "<type>")
    private SubtitleType inputType;

    @Option(names = {"--output-type"}, description = "The type of subtitle file to write to. If not specified, the input type will be used. Valid options: ${COMPLETION-CANDIDATES}", completionCandidates = TypeCompleter.class, paramLabel = "<type>")
    private SubtitleType outputType;

    @Spec
    CommandSpec spec;

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

    static class TypeCompleter implements Iterable<String> {

        @Override
        public Iterator<String> iterator() {
            return Arrays.stream(SubtitleType.values())
                .map(SubtitleType::name)
                .iterator();
        }
    }

    static class FileCompleter implements Iterable<String> {

        @Override
        public Iterator<String> iterator() {
            File[] files = new File(".").listFiles(File::isFile);
            if (files == null) {
                return Collections.emptyIterator();
            }
            return Arrays.stream(files)
                .map(File::getName)
                .filter(f -> Arrays.stream(SubtitleType.values()).anyMatch(t -> f.endsWith(t.getExtension())))
                .iterator();
        }
    }

}
