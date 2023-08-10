package sh.casey.subtitler.renumberer;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleRenumbererFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleRenumberer<T> getInstance(final SubtitleType type) {
        if (type == SubtitleType.SRT) {
            return (SubtitleRenumberer<T>) new SrtSubtitleRenumberer();
        } else if (type == SubtitleType.ASS) {
            return (SubtitleRenumberer<T>) new AssSubtitleRenumberer();
        } else if (type == SubtitleType.SSA) {
            return (SubtitleRenumberer<T>) new AssSubtitleRenumberer();
        } else {
            throw new IllegalArgumentException("There is no renumberer for type " + type);
        }
    }
}
