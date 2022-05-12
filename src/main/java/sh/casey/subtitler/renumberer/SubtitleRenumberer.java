package sh.casey.subtitler.renumberer;

import sh.casey.subtitler.model.SubtitleFile;

public interface SubtitleRenumberer<T extends SubtitleFile> {

    default void renumber(T file) {
        renumber(file, 1);
    }

    void renumber(T file, int start);
}
