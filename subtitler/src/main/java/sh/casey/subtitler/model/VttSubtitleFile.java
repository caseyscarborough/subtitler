package sh.casey.subtitler.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
public class VttSubtitleFile extends BaseSubtitleFile {

    private final Map<String, String> metadata = new TreeMap<>();

    private final List<VttSubtitle> subtitles = new ArrayList<>();

    @Override
    public SubtitleType getType() {
        return SubtitleType.VTT;
    }
}
