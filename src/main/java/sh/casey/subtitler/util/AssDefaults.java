package sh.casey.subtitler.util;

import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;

import java.util.Arrays;
import java.util.List;

public class AssDefaults {

    public static AssSubtitleFile getDefaultAssSubtitleFile() {
        final AssSubtitleFile file = new AssSubtitleFile();
        file.setScriptType("v4.00+");
        file.setWrapStyle("0");
        file.setScaledBorderAndShadow("yes");
        file.setCollisions("Normal");
        file.setStylesFormatOrder(getDefaultStylingFormatOrder());
        file.getStyles().add(getDefaultStyling());
        file.getStyles().add(getItalicStyling());
        file.setEventsFormatOrder(getDefaultEventsFormatOrder());
        return file;
    }

    public static List<String> getDefaultStylingFormatOrder() {
        return Arrays.asList(
            "Name",
            "Fontname",
            "Fontsize",
            "PrimaryColour",
            "SecondaryColour",
            "OutlineColour",
            "BackColour",
            "Bold",
            "Italic",
            "Underline",
            "StrikeOut",
            "ScaleX",
            "ScaleY",
            "Spacing",
            "Angle",
            "BorderStyle",
            "Outline",
            "Shadow",
            "Alignment",
            "MarginL",
            "MarginR",
            "MarginV",
            "Encoding");
    }

    public static AssStyle getItalicStyling() {
        final AssStyle styling = getDefaultStyling();
        styling.setItalic("1");
        styling.setName("Italic");
        return styling;
    }

    public static AssStyle getDefaultStyling() {
        final AssStyle style = new AssStyle();
        style.setName("Default");
        style.setFontName("Arial");
        style.setFontSize("18");
        style.setPrimaryColor("&H00FFFFFF");
        style.setSecondaryColor("&H00FFFFFF");
        style.setOutlineColor("&H00000000");
        style.setBackColor("&H00000000");
        style.setBold("-1");
        style.setItalic("0");
        style.setUnderline("0");
        style.setStrikeOut("0");
        style.setScaleX("100");
        style.setScaleY("100");
        style.setSpacing("0");
        style.setAngle("0");
        style.setBorderStyle("1");
        style.setOutline("1");
        style.setShadow("0");
        style.setAlignment("2");
        style.setMarginL("60");
        style.setMarginR("60");
        style.setMarginV("15");
        style.setEncoding("0");
        return style;
    }

    public static List<String> getDefaultEventsFormatOrder() {
        return Arrays.asList(
            "Layer",
            "Start",
            "End",
            "Style",
            "Name",
            "MarginL",
            "MarginR",
            "MarginV",
            "Effect",
            "Text");
    }

    public static AssDialogue getDefaultDialogue() {
        final AssDialogue dialogue = new AssDialogue();
        dialogue.setLayer("0");
        dialogue.setStyle("Default");
        dialogue.setMarginL("0000");
        dialogue.setMarginR("0000");
        dialogue.setMarginV("0000");
        dialogue.setEffect("");
        dialogue.setActor("");
        return dialogue;
    }

    public static AssDialogue getItalicDialogue() {
        final AssDialogue dialogue = getDefaultDialogue();
        dialogue.setStyle("Italic");
        return dialogue;
    }

    public static List<AssStyle> getDefaultStylesWithTopAndBottom() {
        final AssStyle top = AssDefaults.getDefaultStyling();
        top.setName("Top");
        top.setAlignment("8");

        final AssStyle bottom = AssDefaults.getDefaultStyling();
        bottom.setName("Bottom");
        bottom.setAlignment("2");

        return Arrays.asList(top, bottom);
    }
}
