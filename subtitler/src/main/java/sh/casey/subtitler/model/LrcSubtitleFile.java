package sh.casey.subtitler.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
public class LrcSubtitleFile extends BaseSubtitleFile {

    private final Map<String, String> metadata = new LinkedHashMap<>();
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
