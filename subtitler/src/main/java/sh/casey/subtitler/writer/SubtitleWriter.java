package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.SubtitleFile;

import java.io.Writer;

public interface SubtitleWriter<T extends SubtitleFile> {

    void write(T file, String outputPath);

    void write(T file, Writer writer);
}
