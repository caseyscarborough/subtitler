package sh.casey.subtitler.model;

import sh.casey.subtitler.util.TimeUtil;

public class DxfpSubtitle implements Subtitle {

    private String id;
    private Integer number;
    private String text;
    private String start;
    private String end;
    private String region;
    private boolean italic = false;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.DFXP;
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
        return TimeUtil.assFormatTimeToMilliseconds(getStart());
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
        return TimeUtil.assFormatTimeToMilliseconds(getEnd());
    }

    @Override
    public void setEnd(final String end) {
        this.end = end;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(final boolean italic) {
        this.italic = italic;
    }
}
