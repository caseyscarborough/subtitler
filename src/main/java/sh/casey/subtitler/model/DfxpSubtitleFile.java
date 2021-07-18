package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.List;

public class DfxpSubtitleFile implements SubtitleFile {

    private List<DxfpSubtitle> subtitles = new ArrayList<>();

    @Override
    public List<DxfpSubtitle> getSubtitles() {
        return subtitles;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.DFXP;
    }
}
