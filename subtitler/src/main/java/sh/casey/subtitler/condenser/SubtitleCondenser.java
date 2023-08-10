package sh.casey.subtitler.condenser;

import sh.casey.subtitler.model.SubtitleFile;

public interface SubtitleCondenser<T extends SubtitleFile> {

    T condense(T input);
}
