package sh.casey.subtitler.filter;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class FiltererFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> Filterer<T>  getInstance(SubtitleType type) {
        if (type == SubtitleType.ASS) {
            return (Filterer<T>) new AssFilterer();
        } else if (type == SubtitleType.SSA) {
            return (Filterer<T>) new AssFilterer();
        } else if (type == SubtitleType.SRT) {
            return (Filterer<T>) new SrtFilterer();
        } else {
            throw new IllegalStateException("Could not find filterer for subtitle type " + type);
        }
    }
}
