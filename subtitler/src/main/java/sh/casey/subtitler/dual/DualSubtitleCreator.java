package sh.casey.subtitler.dual;

import sh.casey.subtitler.converter.SubtitleConverterFactory;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.util.AssDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class works for the majority of cases, but it may cause
 * strange results in the following situations:
 */
public class DualSubtitleCreator {

    private final SubtitleConverterFactory converterFactory = new SubtitleConverterFactory();

    public SubtitleFile create(SubtitleFile top, SubtitleFile bottom) {
        return create(top, bottom, DualSubtitleConfig.builder().build());
    }

    public SubtitleFile create(SubtitleFile top, SubtitleFile bottom, DualSubtitleConfig config) {
        SubtitleType topType = top.getType();
        SubtitleType bottomType = bottom.getType();
        final AssSubtitleFile topFile = getAssSubtitleFile(top);
        final AssSubtitleFile bottomFile = getAssSubtitleFile(bottom);

        AssSubtitleFile output;
        if (bottomType == SubtitleType.ASS) {
            // If the bottom file was already in ASS format, we will reuse all the styles
            // in the output file
            output = bottomFile;
        } else {
            // Otherwise we'll create a new one with defaults
            output = AssDefaults.getDefaultAssSubtitleFile();
            output.getStyles().add(AssDefaults.getDefaultBottomStyle());
            bottomFile.getDialogues().forEach(d -> d.setStyle("Bottom_Default"));
        }

        // configure top style
        if (config.isKeepTopStyles() && topType == SubtitleType.ASS) {
            // prefix the styles with Top_, so they don't clash with the bottom file.
            topFile.getStyles().forEach(s -> s.setName("Top_" + s.getName()));
            topFile.getStyles().stream().filter(s -> s.getAlignment().equals("2")).forEach(s -> s.setAlignment("8"));
            topFile.getDialogues().forEach(d -> d.setStyle("Top_" + d.getStyle()));
            output.getStyles().addAll(topFile.getStyles());
        } else {
            final AssStyle topStyle;
            if (config.getCopyStyleFrom() != null) {
                topStyle = bottomFile
                    .getStyles()
                    .stream()
                    .filter(s -> s.getName().equals(config.getCopyStyleFrom()))
                    .findFirst()
                    .orElseThrow(() -> new SubtitleException("Could not find " + config.getCopyStyleFrom() + " style in the bottom file!"));
            } else {
                topStyle = AssDefaults.getDefaultTopStyle();
            }
            topStyle.setName("Top_Default");
            for (Map.Entry<String, String> entry : config.getTopStyleConfig().entrySet()) {
                topStyle.setAttribute(entry.getKey(), handleBoolean(entry.getValue()));
            }
            output.getStyles().add(topStyle);
            // set all dialogues for the top file to the top style
            topFile.getDialogues().forEach(d -> d.setStyle("Top_Default"));
        }

        final List<AssDialogue> dialogues = new ArrayList<>();
        dialogues.addAll(topFile.getDialogues());
        dialogues.addAll(bottomFile.getDialogues());
        Collections.sort(dialogues);
        output.setDialogues(dialogues);

        if (config.isAlign()) {
            // Move all top subtitles from the bottom file to the bottom
            // This can have some strange side effects if your bottom file
            // is heavily styled and uses the 8 alignment for more than just
            // text at the top of the screen.
            output.getStyles()
                .stream()
                .filter(s -> !s.getName().startsWith("Top_") && s.getAlignment().equals("8"))
                .forEach(s -> s.setAlignment("2"));
            output.getDialogues()
                .stream()
                .filter(d -> d.getText().contains("\\an8"))
                .forEach(d -> d.setText(d.getText().replace("{\\an8}", "").replace("\\an8", "")));
        }
        return output;
    }

    private AssSubtitleFile getAssSubtitleFile(SubtitleFile file) {
        if (file.getType() == SubtitleType.ASS) {
            return new AssSubtitleFile((AssSubtitleFile) file);
        }
        return (AssSubtitleFile) converterFactory.getInstance(file.getType(), SubtitleType.ASS).convert(file);
    }

    private String handleBoolean(final String value) {
        if ("true".equalsIgnoreCase(value)) {
            return "-1";
        }
        if ("false".equalsIgnoreCase(value)) {
            return "0";
        }
        return value;
    }
}
