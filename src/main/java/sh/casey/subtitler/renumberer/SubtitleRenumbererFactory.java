package sh.casey.subtitler.renumberer;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleRenumbererFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleRenumberer<T> getInstance(SubtitleType type) {
        if (type == SubtitleType.SRT) {
            return (SubtitleRenumberer<T>) new SrtSubtitleRenumberer();
        } else {
            throw new IllegalArgumentException("There is no renumberer for type " + type);
        }
    }
}
