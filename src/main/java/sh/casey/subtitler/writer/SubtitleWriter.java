package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.SubtitleFile;

public interface SubtitleWriter<T extends SubtitleFile> {

    void write(T file, String outputPath);
}
