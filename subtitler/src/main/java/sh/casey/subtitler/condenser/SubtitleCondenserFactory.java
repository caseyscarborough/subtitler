package sh.casey.subtitler.condenser;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleCondenserFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleCondenser<T> getInstance(final SubtitleType type) {
        if (type == SubtitleType.SRT) {
            return (SubtitleCondenser<T>) new SrtSubtitleCondenser();
        } else if (type == SubtitleType.ASS || type == SubtitleType.SSA) {
            return (SubtitleCondenser<T>) new AssSubtitleCondenser();
        } else {
            throw new IllegalArgumentException("No condenser found for type " + type);
        }
    }
}
