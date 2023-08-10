package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.List;

public class TtmlSubtitleFile extends BaseSubtitleFile {

    private List<TtmlSubtitle> subtitles = new ArrayList<>();

    @Override
    public List<TtmlSubtitle> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(final List<TtmlSubtitle> subtitles) {
        this.subtitles = subtitles;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.TTML;
    }
}
