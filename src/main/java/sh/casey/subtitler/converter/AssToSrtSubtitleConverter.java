package sh.casey.subtitler.converter;

import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.renumberer.SrtSubtitleRenumberer;
import sh.casey.subtitler.util.SubtitleUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssToSrtSubtitleConverter implements SubtitleConverter<AssSubtitleFile, SrtSubtitleFile> {

    private static final Pattern COLOR_PATTERN = Pattern.compile(".*\\\\c&H([A-Fa-f0-9]{2,6})&.*");

    @Override
    public SrtSubtitleFile convert(AssSubtitleFile input) {
        SrtSubtitleFile output = new SrtSubtitleFile();

        final Map<String, AssStyle> styles = new HashMap<>();
        input.getStyles().forEach(s -> styles.put(s.getName(), s));

        for (AssDialogue dialogue : input.getSubtitles()) {
            if (StringUtils.isBlank(dialogue.getText()) || dialogue.isComment()) {
                continue;
            }
            final AssStyle style = styles.get(dialogue.getStyle());
            String text = getSrtText(dialogue.getText(), style);

            SrtSubtitle subtitle = new SrtSubtitle();
            subtitle.setText(text);
            subtitle.setStart(SubtitleUtils.convertAssTimeToSrtTime(dialogue.getStart()));
            subtitle.setEnd(SubtitleUtils.convertAssTimeToSrtTime(dialogue.getEnd()));
            subtitle.setNumber(dialogue.getNumber());
            output.getSubtitles().add(subtitle);
        }

        // Renumber all lines in case we removed any blank lines.
        SrtSubtitleRenumberer renumberer = new SrtSubtitleRenumberer();
        renumberer.renumber(output);
        return output;
    }

    String getSrtText(final String text, final AssStyle style) {
        String output = text
            .replace("{\\i1}", "<i>")
            .replace("{\\i0}", "</i>")
            .replace("{\\b1}", "<b>")
            .replace("{\\b0}", "</b>")
            .replace("{\\u1}", "<u>")
            .replace("{\\u0}", "</u>")
            .replace("\\N", "\n");

        Matcher m = COLOR_PATTERN.matcher(output);
        output = output.replaceAll("\\{.+?}", "");
        boolean colored = false;
        if (m.matches()) {
            final String hex = convertAssColorToHex(m.group(1));
            output = "<font color=\"#" + hex + "\">" + output + "</font>";
            colored = true;
        }

        if (style == null) {
            return output;
        }

        // Yes, -1 is true. 0 is false.
        if (style.isBold()) {
            output = "<b>" + output + "</b>";
        }

        if (style.isItalic()) {
            output = "<i>" + output + "</i>";
        }

        if (style.isUnderline()) {
            output = "<u>" + output + "</u>";
        }

        // We won't color based on style if we've already used the color from the {\c} tag.
        // .ass colors are in reverse: &H00012345 = #452301
        if (!colored && style.getPrimaryColor() != null && !style.getPrimaryColor().toLowerCase(Locale.ROOT).endsWith("ffffff") && style.getPrimaryColor().length() == 10) {
            String hex = style.getPrimaryColor().toLowerCase(Locale.ROOT).substring(4);
            String newHex = hex.substring(4, 6) + hex.substring(2, 4) + hex.substring(0, 2);
            output = "<font color=\"#" + newHex + "\">" + output + "</font>";
        }

        return output;
    }

    String convertAssColorToHex(final String color) {
        final String hex = StringUtils.leftPad(color, 6, "0").toLowerCase(Locale.ROOT);
        if (hex.length() != 6) {
            return "";
        }
        return hex.substring(4, 6) + hex.substring(2, 4) + hex.substring(0, 2);
    }
}
