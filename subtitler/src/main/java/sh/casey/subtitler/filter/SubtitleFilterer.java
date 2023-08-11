package sh.casey.subtitler.filter;

import sh.casey.subtitler.model.SubtitleFile;

import java.util.List;
import java.util.Map;

public interface SubtitleFilterer<T extends SubtitleFile> {

    default void filter(T file, Map<FilterType, List<String>> filters, FilterMode mode) {
        filter(file, filters, mode, Integer.MAX_VALUE);
    }

    void filter(T file, Map<FilterType, List<String>> filters, FilterMode mode, int threshold);
}
