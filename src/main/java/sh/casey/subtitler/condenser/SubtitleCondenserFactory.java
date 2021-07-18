package sh.casey.subtitler.condenser;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleCondenserFactory {

    public <T extends SubtitleFile> SubtitleCondenser<T> getInstance(SubtitleType type) {
        if (type == SubtitleType.SRT) {
            return (SubtitleCondenser<T>) new SrtSubtitleCondenser();
        } else {
            throw new IllegalArgumentException("No condensor found for type " + type);
        }
    }
}
