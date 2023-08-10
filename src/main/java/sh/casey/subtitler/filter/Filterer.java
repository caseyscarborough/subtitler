package sh.casey.subtitler.filter;

import sh.casey.subtitler.model.SubtitleFile;

import java.util.List;
import java.util.Map;

public interface Filterer<T extends SubtitleFile> {

    void filter(T file, Map<FilterType, List<String>> filters, FilterMode mode, int threshold);
}
