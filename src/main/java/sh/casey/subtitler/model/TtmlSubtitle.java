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

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
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
}
