package sh.casey.subtitler.model;

import static sh.casey.subtitler.util.TimeUtil.assFormatTimeToMilliseconds;

public class AssSubtitle implements Subtitle {

    private String start;
    private String end;
    private String text;

    public AssSubtitle() {
    }

    public AssSubtitle(final String start, final String end, final String text) {
        this.start = start;
        this.end = end;
        this.text = text;
    }

    @Override
    public String getStart() {
        return start;
    }

    public void setStart(final String start) {
        this.start = start;
    }

    public Long getStartMilliseconds() {
        return assFormatTimeToMilliseconds(start);
    }

    @Override
    public String getEnd() {
        return end;
    }

    public void setEnd(final String end) {
        this.end = end;
    }

    public Long getEndMilliseconds() {
        return assFormatTimeToMilliseconds(end);
    }

    @Override
    public Integer getNumber() {
        // not implemented
        return null;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.ASS;
    }

    @Override
    public String getText() {
        return text
            .replaceAll("\\{.*?}", "")
            .replaceAll("\\\\N", "\n");
    }

    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Dialogue: 0," + getStart() + "," + getEnd() + ",Default,,0,0,0,," + text + "\n";
    }
}
