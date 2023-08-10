package sh.casey.subtitler.cli.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import sh.casey.subtitler.cli.command.completer.StyleConfigCompleter;
import sh.casey.subtitler.cli.command.completer.SubtitleTypeCompleter;
import sh.casey.subtitler.cli.logging.TerminalLogger;
import sh.casey.subtitler.dual.DualSubtitleConfig;
import sh.casey.subtitler.dual.DualSubtitleCreator;
import sh.casey.subtitler.dual.StyleConfig;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Command(name = "dual", aliases = "d", description = "Create a dual subtitle file from two subtitle files.", sortOptions = false, sortSynopsis = false)
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

    @Option(names = {"-a", "--align"}, description = "Align all subtitles from the original bottom file to the bottom, so as not to clash with the top file.")
    private boolean align = false;

    @Option(names = {"--trace"}, description = "Enable trace logging.")
    private boolean trace;

    @Override
    public void run() {
        if (trace) {
            TerminalLogger.isTraceEnabled = true;
        }

        final SubtitleReaderFactory readerFactory = new SubtitleReaderFactory();
        final SubtitleWriterFactory writerFactory = new SubtitleWriterFactory();
        final DualSubtitleCreator creator = new DualSubtitleCreator();

        log.info("Creating dual subtitle file using top file '" + topPath + "' and bottom file '" + bottomPath + "'");
        final SubtitleReader<SubtitleFile> topReader = readerFactory.getInstance(getTopType());
        final SubtitleReader<SubtitleFile> bottomReader = readerFactory.getInstance(getBottomType());
        SubtitleFile topFile = topReader.read(topPath);
        SubtitleFile bottomFile = bottomReader.read(bottomPath);

        final Map<StyleConfig, String> styles = parseStyleConfiguration();
        final DualSubtitleConfig config = DualSubtitleConfig.builder()
            .keepTopStyles(keepTopStyles)
            .align(align)
            .topStyles(styles)
            .build();
        SubtitleFile output = creator.create(topFile, bottomFile, config);
        final SubtitleWriter<SubtitleFile> writer = writerFactory.getInstance(SubtitleType.ASS);
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

    private Map<StyleConfig, String> parseStyleConfiguration() {
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
        return styles;
    }
}
