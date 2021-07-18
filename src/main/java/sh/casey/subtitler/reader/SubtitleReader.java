package sh.casey.subtitler.reader;

import sh.casey.subtitler.model.SubtitleFile;

public interface SubtitleReader<F extends SubtitleFile> {

    F read(String filename);
}
