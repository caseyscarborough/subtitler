package sh.casey.subtitler.model;

import java.util.List;

public interface SubtitleFile {

    List<? extends Subtitle> getSubtitles();

    SubtitleType getType();

    String getPath();
}
