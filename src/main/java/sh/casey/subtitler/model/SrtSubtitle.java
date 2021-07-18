package sh.casey.subtitler.model;

import sh.casey.subtitler.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class SrtSubtitle implements Subtitle, Comparable<SrtSubtitle> {

    private Integer number;
    private String start;
    private String end;
    private List<String> lines = new ArrayList<>();
    private SubtitlePosition position;

    @Override
    public Integer getNumber() {
        return number;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    public String getFromTimeForAssFormat() {
        return start.replace(",", ".").substring(1, start.length() - 1);
    }

    @Override
    public String getStart() {
        return start;
    }

    @Override
    public void setStart(final String start) {
        this.start = start;
    }

    public Long getStartMilliseconds() {
        return TimeUtil.assFormatTimeToMilliseconds(getFromTimeForAssFormat());
    }

    @Override
    public String getEnd() {
        return end;
    }

    @Override
    public void setEnd(final String end) {
        this.end = end;
    }

    public Long getEndMilliseconds() {
        return TimeUtil.assFormatTimeToMilliseconds(getToTimeForAssFormat());
    }

    public String getToTimeForAssFormat() {
        return end.replace(",", ".").substring(1, end.length() - 1);
    }

    public String getLines() {
        return String.join("\n", lines);
    }

    public String getTextForAss() {
        return getLines()
            .replace("<i>", "{\\i1}")
            .replace("</i>", "{\\i0}")
            .replace("<b>", "{\\b1}")
            .replace("</b>", "{\\b0}")
            .replace("\n", "\\N");
    }

    public void addLine(final String text) {
        this.lines.add(text);
    }

    public SubtitlePosition getPosition() {
        return position;
    }

    public void setPosition(final SubtitlePosition position) {
        this.position = position;
    }

    public String toAss() {
        return "Dialogue: 0," + getFromTimeForAssFormat() + "," + getToTimeForAssFormat() + "," + getPosition() + ",,0000,0000,0000,," + getTextForAss();
    }

    public boolean isValid() {
        return start != null &&
            end != null &&
            !getLines().trim().equals("");
    }

    @Override
    public int compareTo(final SrtSubtitle o) {
        return this.getFromTimeForAssFormat().compareTo(o.getFromTimeForAssFormat());
    }

    @Override
    public String toString() {
        if (!getStart().equals(getEnd())) {
            return getNumber() + "\n" +
                getStart() + " --> " + getEnd() + "\n" +
                getLines() + "\n\n";
        }
        return "";
    }

    @Override
    public String getText() {
        return getLines();
    }

    @Override
    public void setText(final String text) {
        lines = new ArrayList<>();
        lines.add(text);
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.SRT;
    }
}
