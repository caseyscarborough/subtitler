package sh.casey.subtitler.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.converter.SubtitleConverter;
import sh.casey.subtitler.converter.SubtitleConverterFactory;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssScriptInfo;
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
import java.util.List;

@Slf4j
public class DualSubCreator {

    private final DualSubConfig config;
    private final String topPath;
    private final String bottomPath;
    private final SubtitleType topType;
    private final SubtitleType bottomType;
    private final String outputPath;
    private final AssScriptInfo scriptInfo;
    private final CommandLine cmd;

    private DualSubCreator(final CommandLine cmd, final DualSubConfig config, final String topPath, final String bottomPath, final SubtitleType topType, final SubtitleType bottomType, final String outputPath, final AssScriptInfo scriptInfo) {
        this.cmd = cmd;
        this.config = config;
        this.topPath = topPath;
        this.bottomPath = bottomPath;
        this.topType = topType;
        this.bottomType = bottomType;
        this.outputPath = outputPath;
        this.scriptInfo = scriptInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void create() {
        log.info("Creating dual subtitle file using top file '" + topPath + "' and bottom file '" + bottomPath + "'");
        final SubtitleReader topReader = new SubtitleReaderFactory().getInstance(topType);
        final SubtitleReader bottomReader = new SubtitleReaderFactory().getInstance(bottomType);
        SubtitleFile topFile = topReader.read(topPath);
        SubtitleFile bottomFile = bottomReader.read(bottomPath);

        if (topType != SubtitleType.ASS) {
            final SubtitleConverter converter = new SubtitleConverterFactory().getInstance(topType, SubtitleType.ASS);
            topFile = converter.convert(topFile);
        }

        if (bottomType != SubtitleType.ASS) {
            final SubtitleConverter converter = new SubtitleConverterFactory().getInstance(bottomType, SubtitleType.ASS);
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
        boolean keepTopStyles = cmd.hasOption("--keep-top-styles") && topType == SubtitleType.ASS;
        if (keepTopStyles) {
            // prefix the styles with Top_, so they don't clash with the bottom file.
            top.getStyles().forEach(s -> {
                s.setName("Top_" + s.getName());
                s.setAlignment("8");
            });
            top.getDialogues().forEach(d -> d.setStyle("Top_" + d.getStyle()));
            output.getStyles().addAll(top.getStyles());
        } else {
            final AssStyle topStyle = AssDefaults.getDefaultTopStyle();
            if (config.getFont() != null) {
                topStyle.setFontName(config.getFont());
            }
            if (config.getSize() != null) {
                topStyle.setFontSize(config.getSize());
            }
            if (config.getOutline() != null) {
                topStyle.setOutline(String.valueOf(config.getOutline()));
            }
            topStyle.setBold(config.isBold());
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
        log.debug("Done.");
    }

    public static class Builder {
        private DualSubConfig config;
        private String top;
        private String bottom;
        private SubtitleType topType;
        private SubtitleType bottomType;
        private String output;
        private AssScriptInfo scriptInfo;
        private CommandLine cmd;

        Builder() {
        }

        public Builder cmd(final CommandLine cmd) {
            this.cmd = cmd;
            return this;
        }

        public Builder config(final DualSubConfig config) {
            this.config = config;
            return this;
        }

        public Builder topFile(final String file) {
            top = file;
            if (file.endsWith("srt")) {
                topType(SubtitleType.SRT);
            } else if (file.endsWith("ass")) {
                topType(SubtitleType.ASS);
            }
            return this;
        }

        public Builder bottomFile(final String file) {
            bottom = file;
            if (file.endsWith("srt")) {
                bottomType(SubtitleType.SRT);
            } else if (file.endsWith("ass")) {
                bottomType(SubtitleType.ASS);
            }
            return this;
        }

        public Builder outputFile(final String file) {
            output = file;
            return this;
        }

        public Builder topType(final SubtitleType topType) {
            this.topType = topType;
            return this;
        }

        public Builder bottomType(final SubtitleType bottomType) {
            this.bottomType = bottomType;
            return this;
        }

        public Builder scriptInfo(final AssScriptInfo scriptInfo) {
            this.scriptInfo = scriptInfo;
            return this;
        }

        public DualSubCreator build() {
            if (top == null) {
                throw new IllegalStateException("Top file must be present to build Dual Sub creator.");
            }

            if (bottom == null) {
                throw new IllegalStateException("Bottom file must be present to build Dual Sub creator.");
            }

            if (output == null) {
                throw new IllegalStateException("Output file must be present to build Dual Sub creator.");
            }

            if (config == null) {
                config = DualSubConfig.builder().build();
            }
            return new DualSubCreator(cmd, config, top, bottom, topType, bottomType, output, scriptInfo);
        }
    }
}
