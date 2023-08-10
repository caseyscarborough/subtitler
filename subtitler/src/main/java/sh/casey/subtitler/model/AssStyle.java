package sh.casey.subtitler.model;

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class AssStyle {

    private final Map<String, String> attributes = new HashMap<>();

    public AssStyle(AssStyle style) {
        this.attributes.putAll(style.getAttributes());
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getName() {
        return attributes.get("Name");
    }

    public void setName(final String name) {
        attributes.put("Name", name);
    }

    public String getFontName() {
        return attributes.get("Fontname");
    }

    public void setFontName(final String fontName) {
        attributes.put("Fontname", fontName);
    }

    public String getFontSize() {
        return attributes.get("Fontsize");
    }

    public void setFontSize(final String fontSize) {
        attributes.put("Fontsize", fontSize);
    }

    public String getPrimaryColor() {
        return attributes.get("PrimaryColour");
    }

    public void setPrimaryColor(final String primaryColor) {
        attributes.put("PrimaryColour", primaryColor);
    }

    public String getSecondaryColor() {
        return attributes.get("SecondaryColour");
    }

    public void setSecondaryColor(final String secondaryColor) {
        attributes.put("SecondaryColour", secondaryColor);
    }

    public String getTertiaryColor() {
        return attributes.get("TertiaryColour");
    }

    public void setTertiaryColor(final String tertiaryColor) {
        attributes.put("TertiaryColour", tertiaryColor);
    }

    public String getOutlineColor() {
        return attributes.get("OutlineColour");
    }

    public void setOutlineColor(final String outlineColor) {
        attributes.put("OutlineColour", outlineColor);
    }

    public String getBackColor() {
        return attributes.get("BackColour");
    }

    public void setBackColor(final String backColor) {
        attributes.put("BackColour", backColor);
    }

    public boolean isBold() {
        // -1 is true, 0 is false.
        return "-1".equals(attributes.get("Bold"));
    }

    public void setBold(final boolean bold) {
        // -1 is true, 0 is false.
        attributes.put("Bold", bold ? "-1" : "0");
    }

    public void setBold(String bold) {
        // -1 is true, 0 is false
        attributes.put("Bold", bold.equals("true") || bold.equals("-1") ? "-1" : "0");
    }

    public boolean isItalic() {
        return "-1".equals(attributes.get("Italic"));
    }

    public void setItalic(final boolean italic) {
        attributes.put("Italic", italic ? "-1" : "0");
    }

    public boolean isUnderline() {
        return "-1".equals(attributes.get("Underline"));
    }

    public void setUnderline(final boolean underline) {
        attributes.put("Underline", underline ? "-1" : "0");
    }

    public String getStrikeOut() {
        return attributes.get("StrikeOut");
    }

    public void setStrikeOut(final String strikeOut) {
        attributes.put("StrikeOut", strikeOut);
    }

    public String getScaleX() {
        return attributes.get("ScaleX");
    }

    public void setScaleX(final String scaleX) {
        attributes.put("ScaleX", scaleX);
    }

    public String getScaleY() {
        return attributes.get("ScaleY");
    }

    public void setScaleY(final String scaleY) {
        attributes.put("ScaleY", scaleY);
    }

    public String getSpacing() {
        return attributes.get("Spacing");
    }

    public void setSpacing(final String spacing) {
        attributes.put("Spacing", spacing);
    }

    public String getAngle() {
        return attributes.get("Angle");
    }

    public void setAngle(final String angle) {
        attributes.put("Angle", angle);
    }

    public String getBorderStyle() {
        return attributes.get("BorderStyle");
    }

    public void setBorderStyle(final String borderStyle) {
        attributes.put("BorderStyle", borderStyle);
    }

    public String getOutline() {
        return attributes.get("Outline");
    }

    public void setOutline(final String outline) {
        attributes.put("Outline", outline);
    }

    public String getShadow() {
        return attributes.get("Shadow");
    }

    public void setShadow(final String shadow) {
        attributes.put("Shadow", shadow);
    }

    public String getAlignment() {
        return attributes.get("Alignment");
    }

    public void setAlignment(final String alignment) {
        attributes.put("Alignment", alignment);
    }

    public String getMarginL() {
        return attributes.get("MarginL");
    }

    public void setMarginL(final String marginL) {
        attributes.put("MarginL", marginL);
    }

    public String getMarginR() {
        return attributes.get("MarginR");
    }

    public void setMarginR(final String marginR) {
        attributes.put("MarginR", marginR);
    }

    public String getMarginV() {
        return attributes.get("MarginV");
    }

    public void setMarginV(final String marginV) {
        attributes.put("MarginV", marginV);
    }

    public String getEncoding() {
        return attributes.get("Encoding");
    }

    public void setEncoding(final String encoding) {
        attributes.put("Encoding", encoding);
    }

    public String getValue(final String formatValue) {
        return attributes.getOrDefault(formatValue, "");
    }

}
