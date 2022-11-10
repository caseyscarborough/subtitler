package sh.casey.subtitler.model;

public class TtmlSubtitle implements Subtitle {

    private Integer number;
    private String text;
    private String start;
    private String end;

    @Override
    public SubtitleType getType() {
        return SubtitleType.TTML;
    }

    @Override
    public Integer getNumber() {
        return number;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public String getStart() {
        return start;
    }

    @Override
    public Long getStartMilliseconds() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEnd(final String end) {
        this.end = end;
    }
}
