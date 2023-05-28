package sh.casey.subtitler.util;

import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssScriptInfo;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssStyleVersion;
import sh.casey.subtitler.model.AssSubtitleFile;

import java.util.Arrays;
import java.util.List;

public class AssDefaults {

    public static AssSubtitleFile getDefaultAssSubtitleFile() {
        final AssSubtitleFile file = new AssSubtitleFile();
        final AssScriptInfo scriptInfo = new AssScriptInfo();
        scriptInfo.getAttributes().add("ScriptType: v4.00+");
        scriptInfo.getAttributes().add("WrapStyle: 0");
        scriptInfo.getAttributes().add("ScaledBorderAndShadow: yes");
        scriptInfo.getAttributes().add("Collisions: Normal");
        scriptInfo.getAttributes().add("PlayResX: 1920");
        scriptInfo.getAttributes().add("PlayResY: 1080");
        file.setScriptInfo(scriptInfo);
        file.setStylesFormatOrder(getDefaultStylingFormatOrder());
        file.getStyles().add(getDefaultStyling());
        file.getStyles().add(getItalicStyling());
        file.setEventsFormatOrder(getDefaultEventsFormatOrder());
        file.setStyleVersion(AssStyleVersion.V4PLUS);
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
        styling.setItalic(true);
        styling.setName("Italic");
        return styling;
    }

    public static AssStyle getDefaultStyling() {
        final AssStyle style = new AssStyle();
        style.setName("Default");
        style.setFontName("Arial");
        style.setFontSize("64");
        style.setPrimaryColor("&H00FFFFFF");
        style.setSecondaryColor("&H00FFFFFF");
        style.setOutlineColor("&H00000000");
        style.setBackColor("&H00000000");
        style.setBold(false);
        style.setItalic(false);
        style.setUnderline(false);
        style.setStrikeOut("0");
        style.setScaleX("100");
        style.setScaleY("100");
        style.setSpacing("0");
        style.setAngle("0");
        style.setBorderStyle("1");
        style.setOutline("3");
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
        dialogue.setMarginL("0");
        dialogue.setMarginR("0");
        dialogue.setMarginV("0");
        dialogue.setEffect("");
        dialogue.setActor("");
        return dialogue;
    }

    public static AssDialogue getItalicDialogue() {
        final AssDialogue dialogue = getDefaultDialogue();
        dialogue.setStyle("Italic");
        return dialogue;
    }

    public static AssStyle getDefaultTopStyle() {
        final AssStyle top = AssDefaults.getDefaultStyling();
        top.setName("Top");
        top.setAlignment("8");
        return top;
    }

    public static AssStyle getDefaultBottomStyle() {
        final AssStyle bottom = AssDefaults.getDefaultStyling();
        bottom.setName("Bottom");
        bottom.setAlignment("2");
        return bottom;
    }

    public static List<AssStyle> getDefaultStylesWithTopAndBottom() {
        final AssStyle top = getDefaultTopStyle();
        final AssStyle bottom = getDefaultBottomStyle();
        return Arrays.asList(top, bottom);
    }
}
