package sh.casey.subtitler.model;

public interface Subtitle {

    Integer getNumber();

    SubtitleType getType();

    String getText();

    void setText(String text);

    String getStart();

    void setStart(String start);

    String getEnd();

    void setEnd(String end);
}
