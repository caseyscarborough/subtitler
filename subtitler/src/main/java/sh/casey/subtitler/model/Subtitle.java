package sh.casey.subtitler.model;

public interface Subtitle {

    Integer getNumber();

    SubtitleType getType();

    String getText();

    void setText(String text);

    String getStart();

    Long getStartMilliseconds();

    void setStart(String start);

    String getEnd();

    Long getEndMilliseconds();

    void setEnd(String end);
}
