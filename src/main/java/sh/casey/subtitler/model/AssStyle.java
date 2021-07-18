package sh.casey.subtitler.model;

public class AssStyle {

    private String name;
    private String fontName;
    private String fontSize;
    private String primaryColor;
    private String secondaryColor;
    private String outlineColor;
    private String backColor;
    private String bold;
    private String italic;
    private String underline;
    private String strikeOut;
    private String scaleX;
    private String scaleY;
    private String spacing;
    private String angle;
    private String borderStyle;
    private String outline;
    private String shadow;
    private String alignment;
    private String marginL;
    private String marginR;
    private String marginV;
    private String encoding;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(final String fontName) {
        this.fontName = fontName;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(final String fontSize) {
        this.fontSize = fontSize;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(final String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(final String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(final String outlineColor) {
        this.outlineColor = outlineColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(final String backColor) {
        this.backColor = backColor;
    }

    public String getBold() {
        return bold;
    }

    public void setBold(final String bold) {
        this.bold = bold;
    }

    public String getItalic() {
        return italic;
    }

    public void setItalic(final String italic) {
        this.italic = italic;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(final String underline) {
        this.underline = underline;
    }

    public String getStrikeOut() {
        return strikeOut;
    }

    public void setStrikeOut(final String strikeOut) {
        this.strikeOut = strikeOut;
    }

    public String getScaleX() {
        return scaleX;
    }

    public void setScaleX(final String scaleX) {
        this.scaleX = scaleX;
    }

    public String getScaleY() {
        return scaleY;
    }

    public void setScaleY(final String scaleY) {
        this.scaleY = scaleY;
    }

    public String getSpacing() {
        return spacing;
    }

    public void setSpacing(final String spacing) {
        this.spacing = spacing;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(final String angle) {
        this.angle = angle;
    }

    public String getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(final String borderStyle) {
        this.borderStyle = borderStyle;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(final String outline) {
        this.outline = outline;
    }

    public String getShadow() {
        return shadow;
    }

    public void setShadow(final String shadow) {
        this.shadow = shadow;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(final String alignment) {
        this.alignment = alignment;
    }

    public String getMarginL() {
        return marginL;
    }

    public void setMarginL(final String marginL) {
        this.marginL = marginL;
    }

    public String getMarginR() {
        return marginR;
    }

    public void setMarginR(final String marginR) {
        this.marginR = marginR;
    }

    public String getMarginV() {
        return marginV;
    }

    public void setMarginV(final String marginV) {
        this.marginV = marginV;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    public String getValue(final String formatValue) {
        switch (formatValue) {
            case "Name":
                return getName();
            case "Fontname":
                return getFontName();
            case "Fontsize":
                return getFontSize();
            case "PrimaryColour":
                return getPrimaryColor();
            case "SecondaryColour":
                return getSecondaryColor();
            case "OutlineColour":
                return getOutlineColor();
            case "BackColour":
                return getBackColor();
            case "Bold":
                return getBold();
            case "Italic":
                return getItalic();
            case "Underline":
                return getUnderline();
            case "StrikeOut":
                return getStrikeOut();
            case "ScaleX":
                return getScaleX();
            case "ScaleY":
                return getScaleY();
            case "Spacing":
                return getSpacing();
            case "Angle":
                return getAngle();
            case "BorderStyle":
                return getBorderStyle();
            case "Outline":
                return getOutline();
            case "Shadow":
                return getShadow();
            case "Alignment":
                return getAlignment();
            case "MarginL":
                return getMarginL();
            case "MarginR":
                return getMarginR();
            case "MarginV":
                return getMarginV();
            case "Encoding":
                return getEncoding();
            default:
                return "";
        }
    }
}
