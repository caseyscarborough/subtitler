package sh.casey.subtitler.application;

import lombok.extern.slf4j.Slf4j;
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

    private final String top;
    private final String bottom;
    private final SubtitleType topType;
    private final SubtitleType bottomType;
    private final String output;
    private final AssScriptInfo scriptInfo;

    private DualSubCreator(final String top, final String bottom, final SubtitleType topType, final SubtitleType bottomType, final String output, final AssScriptInfo scriptInfo) {
        this.top = top;
        this.bottom = bottom;
        this.topType = topType;
        this.bottomType = bottomType;
        this.output = output;
        this.scriptInfo = scriptInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void create() {
        log.info("Creating dual subtitle file using top file '" + top + "' and bottom file '" + bottom + "'");
        final SubtitleReader topReader = new SubtitleReaderFactory().getInstance(topType);
        final SubtitleReader bottomReader = new SubtitleReaderFactory().getInstance(bottomType);
        SubtitleFile topFile = topReader.read(top);
        SubtitleFile bottomFile = bottomReader.read(bottom);

        if (topType != SubtitleType.ASS) {
            final SubtitleConverter converter = new SubtitleConverterFactory().getInstance(topType, SubtitleType.ASS);
            topFile = converter.convert(topFile);
        }

        if (bottomType != SubtitleType.ASS) {
            final SubtitleConverter converter = new SubtitleConverterFactory().getInstance(bottomType, SubtitleType.ASS);
            bottomFile = converter.convert(bottomFile);
        }

        final AssSubtitleFile topAss = (AssSubtitleFile) topFile;
        final AssSubtitleFile bottomAss = (AssSubtitleFile) bottomFile;

        AssSubtitleFile outputAss;
        if (bottomType == SubtitleType.ASS) {
            outputAss = bottomAss;
            final AssStyle topStyle = AssDefaults.getDefaultTopStyle();
            topStyle.setFontSize(bottomAss.getStyles().get(0).getFontSize());
            outputAss.getStyles().add(topStyle);
        } else {
            outputAss = AssDefaults.getDefaultAssSubtitleFile();
            outputAss.getStyles().addAll(AssDefaults.getDefaultStylesWithTopAndBottom());
            bottomAss.getDialogues().forEach(d -> d.setStyle("Bottom"));
        }
        topAss.getDialogues().forEach(d -> d.setStyle("Top"));
        final List<AssDialogue> dialogues = new ArrayList<>();
        dialogues.addAll(topAss.getDialogues());
        dialogues.addAll(bottomAss.getDialogues());
        Collections.sort(dialogues);
        outputAss.setDialogues(dialogues);

        final AssSubtitleWriter writer = new AssSubtitleWriter();
        writer.write(outputAss, output);
        log.debug("Done.");
    }

    public static class Builder {
        private String top;
        private String bottom;
        private SubtitleType topType;
        private SubtitleType bottomType;
        private String output;
        private AssScriptInfo scriptInfo;

        Builder() {
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

            return new DualSubCreator(top, bottom, topType, bottomType, output, scriptInfo);
        }
    }
}
