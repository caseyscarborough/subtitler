package sh.casey.subtitler.renumberer;

import sh.casey.subtitler.model.SubtitleFile;

public interface SubtitleRenumberer<T extends SubtitleFile> {

    void renumber(T file);
}
