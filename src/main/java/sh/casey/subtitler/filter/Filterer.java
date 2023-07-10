package sh.casey.subtitler.filter;

import sh.casey.subtitler.model.SubtitleFile;

public interface Filterer<T extends SubtitleFile> {

    void filter(T file, String filters, FilterMode mode);
}
