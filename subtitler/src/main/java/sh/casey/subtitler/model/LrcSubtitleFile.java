package sh.casey.subtitler.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LrcSubtitleFile extends BaseSubtitleFile {

    private final List<LrcSubtitle> subtitles = new ArrayList<>();

    @Override
    public SubtitleType getType() {
        return SubtitleType.LRC;
    }

    @Override
    public List<LrcSubtitle> getSubtitles() {
        return subtitles;
    }
}
