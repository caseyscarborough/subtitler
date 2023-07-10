package sh.casey.subtitler.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import sh.casey.subtitler.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class VttSubtitle implements Subtitle, Comparable<VttSubtitle> {

    private Integer number;
    private String start;
    private String end;
    private List<String> lines = new ArrayList<>();

    @Override
    public Integer getNumber() {
        return number;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    @Override
    public String getStart() {
        return start;
    }

    @Override
    public Long getStartMilliseconds() {
        return TimeUtil.formatTimeToMilliseconds(SubtitleType.VTT, getStart());
    }

    @Override
    public void setStart(final String start) {
        this.start = start;
    }

    @Override
    public String getEnd() {
        return end;
    }

    @Override
    public Long getEndMilliseconds() {
        return TimeUtil.srtFormatTimeToMilliseconds(getEnd());
    }

    @Override
    public void setEnd(final String end) {
        this.end = end;
    }

    public String getLines() {
        return String.join("\n", lines).trim();
    }

    public void addLine(final String text) {
        this.lines.add(text);
    }

    public boolean isValid() {
        return start != null && end != null && !getLines().equals("");
    }

    @Override
    public int compareTo(final VttSubtitle o) {
        return this.getStart().compareTo(o.getEnd());
    }

    @Override
    public String toString() {
        if (!getStart().equals(getEnd())) {
            return
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VttSubtitle)) return false;
        VttSubtitle that = (VttSubtitle) o;
        return new EqualsBuilder()
            .append(number, that.number)
            .append(start, that.start)
            .append(end, that.end)
            .append(lines, that.lines)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(number)
            .append(start)
            .append(end)
            .append(lines)
            .toHashCode();
    }
}
