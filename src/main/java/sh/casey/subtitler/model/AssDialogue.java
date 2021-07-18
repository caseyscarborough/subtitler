package sh.casey.subtitler.model;

import lombok.ToString;

@ToString
public class AssDialogue implements Subtitle, Comparable<AssDialogue> {

    private Integer number;
    private String layer;
    private String start;
    private String end;
    private String style;
    private String actor;
    private String marginL;
    private String marginR;
    private String marginV;
    private String effect;
    private String text;
    private boolean comment = false;

    @Override
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    @Override
    public String getStart() {
        return start;
    }

    @Override
    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public String getEnd() {
        return end;
    }

    @Override
    public void setEnd(String end) {
        this.end = end;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getMarginL() {
        return marginL;
    }

    public void setMarginL(String marginL) {
        this.marginL = marginL;
    }

    public String getMarginR() {
        return marginR;
    }

    public void setMarginR(String marginR) {
        this.marginR = marginR;
    }

    public String getMarginV() {
        return marginV;
    }

    public void setMarginV(String marginV) {
        this.marginV = marginV;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.ASS;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public String getValue(String formatValue) {
        switch (formatValue) {
            case "Layer":
                return getLayer();
            case "Start":
                return getStart();
            case "End":
                return getEnd();
            case "Style":
                return getStyle();
            case "Name":
            case "Actor":
                return getActor();
            case "MarginL":
                return getMarginL();
            case "MarginR":
                return getMarginR();
            case "MarginV":
                return getMarginV();
            case "Effect":
                return getEffect();
            case "Text":
                return getText();
            default:
                return "";
        }
    }

    @Override
    public int compareTo(AssDialogue o) {
        if (this.getText() == null) {
            this.setText("");
        }

        if (o.getText() == null) {
            o.setText("");
        }

        if (!this.getStart().equals(o.getStart())) {
            return this.getStart().compareTo(o.getStart());
        }

        if (!this.getEnd().equals(o.getEnd())) {
            return this.getEnd().compareTo(o.getEnd());
        }

        return this.getText().compareTo(o.getText());
    }
}
