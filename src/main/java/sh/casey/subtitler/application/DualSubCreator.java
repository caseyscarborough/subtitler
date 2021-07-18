package sh.casey.subtitler.application;

import org.apache.log4j.Logger;
import sh.casey.subtitler.converter.SubtitleConverter;
import sh.casey.subtitler.converter.SubtitleConverterFactory;
import sh.casey.subtitler.model.AssDialogue;
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

public class DualSubCreator {

    private static final Logger LOGGER = Logger.getLogger(DualSubCreator.class);

    private final String top;
    private final String bottom;
    private final SubtitleType topType;
    private final SubtitleType bottomType;
    private final String output;

    private DualSubCreator(String top, String bottom, SubtitleType topType, SubtitleType bottomType, String output) {
        this.top = top;
        this.bottom = bottom;
        this.topType = topType;
        this.bottomType = bottomType;
        this.output = output;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void create() {
        LOGGER.info("Creating dual subtitle file using top file '" + top + "' and bottom file '" + bottom + "'");
        SubtitleReader topReader = new SubtitleReaderFactory().getInstance(topType);
        SubtitleReader bottomReader = new SubtitleReaderFactory().getInstance(bottomType);
        SubtitleFile topFile = topReader.read(top);
        SubtitleFile bottomFile = bottomReader.read(bottom);

        if (topType != SubtitleType.ASS) {
            SubtitleConverter converter = new SubtitleConverterFactory().getInstance(topType, SubtitleType.ASS);
            topFile = converter.convert(topFile);
        }

        if (bottomType != SubtitleType.ASS) {
            SubtitleConverter converter = new SubtitleConverterFactory().getInstance(bottomType, SubtitleType.ASS);
            bottomFile = converter.convert(bottomFile);
        }

        AssSubtitleFile topAss = (AssSubtitleFile) topFile;
        AssSubtitleFile bottomAss = (AssSubtitleFile) bottomFile;

        AssSubtitleFile outputAss = AssDefaults.getDefaultAssSubtitleFile();
        outputAss.getStyles().clear();
        outputAss.getStyles().addAll(AssDefaults.getDefaultStylesWithTopAndBottom());
        topAss.getDialogues().forEach(d -> d.setStyle("Top"));
        bottomAss.getDialogues().forEach(d -> d.setStyle("Bottom"));

        List<AssDialogue> dialogues = new ArrayList<>();
        dialogues.addAll(topAss.getDialogues());
        dialogues.addAll(bottomAss.getDialogues());
        Collections.sort(dialogues);
        outputAss.setDialogues(dialogues);

        AssSubtitleWriter writer = new AssSubtitleWriter();
        writer.write(outputAss, output);
        LOGGER.debug("Done.");
    }

    public static class Builder {
        private String top;
        private String bottom;
        private SubtitleType topType;
        private SubtitleType bottomType;
        private String output;

        Builder() {
        }

        public Builder topFile(String file) {
            top = file;
            if (file.endsWith("srt")) {
                topType(SubtitleType.SRT);
            } else if (file.endsWith("ass")) {
                topType(SubtitleType.ASS);
            }
            return this;
        }

        public Builder bottomFile(String file) {
            bottom = file;
            if (file.endsWith("srt")) {
                bottomType(SubtitleType.SRT);
            } else if (file.endsWith("ass")) {
                bottomType(SubtitleType.ASS);
            }
            return this;
        }

        public Builder outputFile(String file) {
            output = file;
            return this;
        }

        public Builder topType(SubtitleType topType) {
            this.topType = topType;
            return this;
        }

        public Builder bottomType(SubtitleType bottomType) {
            this.bottomType = bottomType;
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

            return new DualSubCreator(top, bottom, topType, bottomType, output);
        }
    }
}
