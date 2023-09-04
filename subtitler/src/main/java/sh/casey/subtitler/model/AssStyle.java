package sh.casey.subtitler.model;

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class AssStyle {

    public static final String NAME = "Name";
    public static final String FONTNAME = "Fontname";
    public static final String FONTSIZE = "Fontsize";
    public static final String PRIMARY_COLOUR = "PrimaryColour";
    public static final String SECONDARY_COLOUR = "SecondaryColour";
    public static final String TERTIARY_COLOUR = "TertiaryColour";
    public static final String OUTLINE_COLOUR = "OutlineColour";
    public static final String BACK_COLOUR = "BackColour";
    public static final String BOLD = "Bold";
    public static final String ITALIC = "Italic";
    public static final String UNDERLINE = "Underline";
    public static final String STRIKE_OUT = "StrikeOut";
    public static final String SCALE_X = "ScaleX";
    public static final String SCALE_Y = "ScaleY";
    public static final String SPACING = "Spacing";
    public static final String ANGLE = "Angle";
    public static final String BORDER_STYLE = "BorderStyle";
    public static final String OUTLINE = "Outline";
    public static final String SHADOW = "Shadow";
    public static final String ALIGNMENT = "Alignment";
    public static final String MARGIN_L = "MarginL";
    public static final String MARGIN_R = "MarginR";
    public static final String MARGIN_V = "MarginV";
    public static final String ENCODING = "Encoding";
    public static final List<String> ATTRIBUTES = Arrays.asList(NAME, FONTNAME, FONTSIZE, PRIMARY_COLOUR, SECONDARY_COLOUR, TERTIARY_COLOUR, OUTLINE_COLOUR, BACK_COLOUR, BOLD, ITALIC, UNDERLINE, STRIKE_OUT, SCALE_X, SCALE_Y, SPACING, ANGLE, BORDER_STYLE, OUTLINE, SHADOW, ALIGNMENT, MARGIN_L, MARGIN_R, MARGIN_V, ENCODING);

    private final Map<String, String> attributes = new HashMap<>();

    public AssStyle(AssStyle style) {
        this.attributes.putAll(style.getAttributes());
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getName() {
        return attributes.get(NAME);
    }

    public void setName(final String name) {
        attributes.put(NAME, name);
    }

    public String getFontName() {
        return attributes.get(FONTNAME);
    }

    public void setFontName(final String fontName) {
        attributes.put(FONTNAME, fontName);
    }

    public String getFontSize() {
        return attributes.get(FONTSIZE);
    }

    public void setFontSize(final String fontSize) {
        attributes.put(FONTSIZE, fontSize);
    }

    public String getPrimaryColor() {
        return attributes.get(PRIMARY_COLOUR);
    }

    public void setPrimaryColor(final String primaryColor) {
        attributes.put(PRIMARY_COLOUR, primaryColor);
    }

    public String getSecondaryColor() {
        return attributes.get(SECONDARY_COLOUR);
    }

    public void setSecondaryColor(final String secondaryColor) {
        attributes.put(SECONDARY_COLOUR, secondaryColor);
    }

    public String getTertiaryColor() {
        return attributes.get(TERTIARY_COLOUR);
    }

    public void setTertiaryColor(final String tertiaryColor) {
        attributes.put(TERTIARY_COLOUR, tertiaryColor);
    }

    public String getOutlineColor() {
        return attributes.get(OUTLINE_COLOUR);
    }

    public void setOutlineColor(final String outlineColor) {
        attributes.put(OUTLINE_COLOUR, outlineColor);
    }

    public String getBackColor() {
        return attributes.get(BACK_COLOUR);
    }

    public void setBackColor(final String backColor) {
        attributes.put(BACK_COLOUR, backColor);
    }

    public boolean isBold() {
        // -1 is true, 0 is false.
        return "-1".equals(attributes.get(BOLD));
    }

    public void setBold(final boolean bold) {
        // -1 is true, 0 is false.
        attributes.put(BOLD, bold ? "-1" : "0");
    }

    public void setBold(String bold) {
        // -1 is true, 0 is false
        setBold(bold.equals("true") || bold.equals("-1"));
    }

    public boolean isItalic() {
        return "-1".equals(attributes.get(ITALIC));
    }

    public void setItalic(final boolean italic) {
        attributes.put(ITALIC, italic ? "-1" : "0");
    }

    public boolean isUnderline() {
        return "-1".equals(attributes.get(UNDERLINE));
    }

    public void setUnderline(final boolean underline) {
        attributes.put(UNDERLINE, underline ? "-1" : "0");
    }

    public String getStrikeOut() {
        return attributes.get(STRIKE_OUT);
    }

    public void setStrikeOut(final String strikeOut) {
        attributes.put(STRIKE_OUT, strikeOut);
    }

    public String getScaleX() {
        return attributes.get(SCALE_X);
    }

    public void setScaleX(final String scaleX) {
        attributes.put(SCALE_X, scaleX);
    }

    public String getScaleY() {
        return attributes.get(SCALE_Y);
    }

    public void setScaleY(final String scaleY) {
        attributes.put(SCALE_Y, scaleY);
    }

    public String getSpacing() {
        return attributes.get(SPACING);
    }

    public void setSpacing(final String spacing) {
        attributes.put(SPACING, spacing);
    }

    public String getAngle() {
        return attributes.get(ANGLE);
    }

    public void setAngle(final String angle) {
        attributes.put(ANGLE, angle);
    }

    public String getBorderStyle() {
        return attributes.get(BORDER_STYLE);
    }

    public void setBorderStyle(final String borderStyle) {
        attributes.put(BORDER_STYLE, borderStyle);
    }

    public String getOutline() {
        return attributes.get(OUTLINE);
    }

    public void setOutline(final String outline) {
        attributes.put(OUTLINE, outline);
    }

    public String getShadow() {
        return attributes.get(SHADOW);
    }

    public void setShadow(final String shadow) {
        attributes.put(SHADOW, shadow);
    }

    public String getAlignment() {
        return attributes.get(ALIGNMENT);
    }

    public void setAlignment(final String alignment) {
        attributes.put(ALIGNMENT, alignment);
    }

    public String getMarginL() {
        return attributes.get(MARGIN_L);
    }

    public void setMarginL(final String marginL) {
        attributes.put(MARGIN_L, marginL);
    }

    public String getMarginR() {
        return attributes.get(MARGIN_R);
    }

    public void setMarginR(final String marginR) {
        attributes.put(MARGIN_R, marginR);
    }

    public String getMarginV() {
        return attributes.get(MARGIN_V);
    }

    public void setMarginV(final String marginV) {
        attributes.put(MARGIN_V, marginV);
    }

    public String getEncoding() {
        return attributes.get(ENCODING);
    }

    public void setEncoding(final String encoding) {
        attributes.put(ENCODING, encoding);
    }

    public String getValue(final String formatValue) {
        return attributes.getOrDefault(formatValue, "");
    }

}
