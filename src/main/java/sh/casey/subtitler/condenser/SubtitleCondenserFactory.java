package sh.casey.subtitler.condenser;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleCondenserFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleCondenser<T> getInstance(final SubtitleType type) {
        if (type == SubtitleType.SRT) {
            return (SubtitleCondenser<T>) new SrtSubtitleCondenser();
        } else {
            throw new IllegalArgumentException("No condenser found for type " + type);
        }
    }
}
