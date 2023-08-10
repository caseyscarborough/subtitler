package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.List;

public class VttSubtitleFile extends BaseSubtitleFile {

    private List<VttSubtitle> subtitles = new ArrayList<>();

    @Override
    public List<VttSubtitle> getSubtitles() {
        return subtitles;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.VTT;
    }
}
