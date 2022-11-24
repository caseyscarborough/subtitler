package sh.casey.subtitler.model;

import lombok.ToString;
import sh.casey.subtitler.util.TimeUtil;

import java.util.HashMap;
import java.util.Map;

@ToString
public class AssDialogue implements Subtitle, Comparable<AssDialogue> {

    private final Map<String, String> attributes = new HashMap<>();
    private boolean comment = false;

    @Override
    public Integer getNumber() {
        String number = attributes.get("Number");
        return number != null ? Integer.parseInt(number) : null;
    }

    public void setNumber(final Integer number) {
        attributes.put("Number", String.valueOf(number));
    }

    public String getLayer() {
        return attributes.get("Layer");
    }

    public void setLayer(final String layer) {
        attributes.put("Layer", layer);
    }

    @Override
    public String getStart() {
        return attributes.get("Start");
    }

    @Override
    public void setStart(final String start) {
        attributes.put("Start", start);
    }

    @Override
    public Long getStartMilliseconds() {
        return TimeUtil.assFormatTimeToMilliseconds(getStart());
    }

    @Override
    public String getEnd() {
        return attributes.get("End");
    }

    @Override
    public void setEnd(final String end) {
        attributes.put("End", end);
    }

    @Override
    public Long getEndMilliseconds() {
        return TimeUtil.assFormatTimeToMilliseconds(getEnd());
    }

    public String getStyle() {
        return getValue("Style");
    }

    public void setStyle(final String style) {
        this.attributes.put("Style", style);
    }

    public String getActor() {
        return this.attributes.getOrDefault("Actor", this.attributes.get("Name"));
    }

    public void setActor(final String actor) {
        this.attributes.put("Actor", actor);
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

    public String getEffect() {
        return attributes.get("Effect");
    }

    public void setEffect(final String effect) {
        attributes.put("Effect", effect);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.ASS;
    }

    public String getText() {
        return attributes.get("Text");
    }

    public void setText(final String text) {
        attributes.put("Text", text);
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(final boolean comment) {
        this.comment = comment;
    }

    public String getValue(final String formatValue) {
        return attributes.getOrDefault(formatValue, "");
    }

    @Override
    public int compareTo(final AssDialogue o) {
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
