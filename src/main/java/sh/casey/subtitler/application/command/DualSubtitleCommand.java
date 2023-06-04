package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import sh.casey.subtitler.application.command.completer.StyleConfigCompleter;
import sh.casey.subtitler.application.command.completer.SubtitleTypeCompleter;
import sh.casey.subtitler.application.command.config.StyleConfig;
import sh.casey.subtitler.converter.SubtitleConverter;
import sh.casey.subtitler.converter.SubtitleConverterFactory;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.util.AssDefaults;
import sh.casey.subtitler.writer.AssSubtitleWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Command(name = "dual", aliases = "d", description = "Create a dual subtitle file from two subtitle files.", sortOptions = false)
public class DualSubtitleCommand implements Runnable {

    @Spec
    CommandSpec spec;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display this help message.")
    private boolean help;

    @Option(names = {"-t", "--top-file"}, description = "The top subtitle file.", required = true, paramLabel = "<file>")
    private String topPath;

    @Option(names = {"-T", "--top-type"}, description = "The top subtitle file type. If not set, the file extension will be used to determine the type. Valid options: ${COMPLETION-CANDIDATES}", paramLabel = "<type>", completionCandidates = SubtitleTypeCompleter.class)
    private SubtitleType topType;

    @Option(names = {"-b", "--bottom-file"}, description = "The bottom subtitle file.", required = true, paramLabel = "<file>")
    private String bottomPath;

    @Option(names = {"-B", "--bottom-type"}, description = "The bottom subtitle file type. If not set, the file extension will be used to determine the type. Valid options: ${COMPLETION-CANDIDATES}", paramLabel = "<type>", completionCandidates = SubtitleTypeCompleter.class)
    private SubtitleType bottomType;

    @Option(names = {"-o", "--output-file"}, description = "The output file.", required = true, paramLabel = "<file>")
    private String outputPath;

    @Option(names = {"-s", "--style"}, description = "Style configuration in the format \"key=value,key=value\", for example. \"bold=true,font=Arial,size=32,outline=3\". This is ignored when using -k/--keep-top-styles. Options are ${COMPLETION-CANDIDATES}.", completionCandidates = StyleConfigCompleter.class, paramLabel = "<config>")
    private String cfg;

    @Option(names = {"-k", "--keep-top-styles"}, description = "Keep the styles from the top file (only available for .ass and .ssa formats). If not set, default styles in combination with the configuration will be applied.")
    private boolean keepTopStyles;

    @Override
    public void run() {
        // Parse the dual subtitles configuration
        Map<StyleConfig, String> styles = new EnumMap<>(StyleConfig.class);
        if (cfg != null) {
            final String[] options = cfg.split(",");
            for (String option : options) {
                if (!option.contains("=")) {
                    throw new IllegalArgumentException("Invalid dual subs option " + option);
                }
                String[] keyValue = option.split("=");
                if (keyValue.length != 2) {
                    throw new IllegalArgumentException("Invalid dual subs option: " + option);
                }
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                final StyleConfig found = StyleConfig.find(key);
                if (found == null) {
                    throw new IllegalArgumentException("Invalid dual subs option \"" + key + "\".");
                }
                if (found.getClazz().equals(Integer.class) && !StringUtils.isNumeric(value)) {
                    throw new IllegalArgumentException("Invalid value for option \"" + key + "\": " + value + ". Expected integer, got " + value.getClass().getSimpleName() + ".");
                }
                if (found.getClazz().equals(Boolean.class) && !value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Invalid value for option \"" + key + "\": " + value + ". Expected boolean, got " + value.getClass().getSimpleName() + ".");
                }
                styles.put(found, value);
            }
        }

        log.info("Creating dual subtitle file using top file '" + topPath + "' and bottom file '" + bottomPath + "'");
        final SubtitleType topType = getTopType();
        final SubtitleType bottomType = getBottomType();
        final SubtitleReader<SubtitleFile> topReader = new SubtitleReaderFactory().getInstance(topType);
        final SubtitleReader<SubtitleFile> bottomReader = new SubtitleReaderFactory().getInstance(bottomType);
        SubtitleFile topFile = topReader.read(topPath);
        SubtitleFile bottomFile = bottomReader.read(bottomPath);

        if (topType != SubtitleType.ASS) {
            final SubtitleConverter<SubtitleFile, SubtitleFile> converter = new SubtitleConverterFactory().getInstance(topType, SubtitleType.ASS);
            topFile = converter.convert(topFile);
        }

        if (bottomType != SubtitleType.ASS) {
            final SubtitleConverter<SubtitleFile, SubtitleFile> converter = new SubtitleConverterFactory().getInstance(bottomType, SubtitleType.ASS);
            bottomFile = converter.convert(bottomFile);
        }

        final AssSubtitleFile top = (AssSubtitleFile) topFile;
        final AssSubtitleFile bottom = (AssSubtitleFile) bottomFile;

        AssSubtitleFile output;
        if (bottomType == SubtitleType.ASS) {
            // If the bottom file is ASS format, we will reuse all the styles
            // in the output file
            output = bottom;
        } else {
            // Otherwise we'll create a new one with defaults
            output = AssDefaults.getDefaultAssSubtitleFile();
            output.getStyles().add(AssDefaults.getDefaultBottomStyle());
            bottom.getDialogues().forEach(d -> d.setStyle("Bottom"));
        }

        // configure top style
        if (keepTopStyles && topType == SubtitleType.ASS) {
            // prefix the styles with Top_, so they don't clash with the bottom file.
            top.getStyles().forEach(s -> {
                s.setName("Top_" + s.getName());
                s.setAlignment("8");
            });
            top.getDialogues().forEach(d -> d.setStyle("Top_" + d.getStyle()));
            output.getStyles().addAll(top.getStyles());
        } else {
            final AssStyle topStyle = AssDefaults.getDefaultTopStyle();
            topStyle.setName("Top");
            for (Map.Entry<StyleConfig, String> entry : styles.entrySet()) {
                entry.getKey().getConsumer().accept(topStyle, entry.getValue());
            }
            output.getStyles().add(topStyle);
            // set all dialogues for the top file to the top style
            top.getDialogues().forEach(d -> d.setStyle("Top"));
        }

        final List<AssDialogue> dialogues = new ArrayList<>();
        dialogues.addAll(top.getDialogues());
        dialogues.addAll(bottom.getDialogues());
        Collections.sort(dialogues);
        output.setDialogues(dialogues);

        final AssSubtitleWriter writer = new AssSubtitleWriter();
        writer.write(output, outputPath);
    }

    private SubtitleType getTopType() {
        if (topType != null) {
            return topType;
        }
        return getTypeFromFilename(topPath);
    }

    private SubtitleType getBottomType() {
        if (bottomType != null) {
            return bottomType;
        }
        return getTypeFromFilename(bottomPath);
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
