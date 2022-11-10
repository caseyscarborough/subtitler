package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.List;

public class SrtSubtitleFile extends BaseSubtitleFile {

    private final List<SrtSubtitle> subtitles = new ArrayList<>();

    @Override
    public List<SrtSubtitle> getSubtitles() {
        return subtitles;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.SRT;
    }

}
