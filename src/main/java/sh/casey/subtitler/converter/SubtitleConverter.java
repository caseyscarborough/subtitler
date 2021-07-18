package sh.casey.subtitler.converter;

import sh.casey.subtitler.model.SubtitleFile;

public interface SubtitleConverter<F extends SubtitleFile, T extends SubtitleFile> {

    T convert(F input);
}
