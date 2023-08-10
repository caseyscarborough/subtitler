package sh.casey.subtitler.filter;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleFiltererFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleFilterer<T> getInstance(SubtitleType type) {
        if (type == SubtitleType.ASS || type == SubtitleType.SSA) {
            return (SubtitleFilterer<T>) new AssSubtitleFilterer();
        } else if (type == SubtitleType.SRT) {
            return (SubtitleFilterer<T>) new SrtSubtitleFilterer();
        } else {
            throw new IllegalStateException("Could not find filterer for subtitle type " + type);
        }
    }
}
