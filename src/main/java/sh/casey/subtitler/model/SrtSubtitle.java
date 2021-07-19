package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.List;

public class SrtSubtitle implements Subtitle, Comparable<SrtSubtitle> {

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
    public void setStart(final String start) {
        this.start = start;
    }

    @Override
    public String getEnd() {
        return end;
    }

    @Override
    public void setEnd(final String end) {
        this.end = end;
    }

    public String getLines() {
        return String.join("\n", lines);
    }

    public void addLine(final String text) {
        this.lines.add(text);
    }

    public boolean isValid() {
        return start != null && end != null && !getLines().trim().equals("");
    }

    @Override
    public int compareTo(final SrtSubtitle o) {
        return this.getStart().compareTo(o.getEnd());
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
