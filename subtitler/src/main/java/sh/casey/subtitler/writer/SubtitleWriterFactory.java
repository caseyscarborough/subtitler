package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class SubtitleWriterFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleWriter<T> getInstance(final SubtitleType type) {
        if (type.equals(SubtitleType.SRT)) {
            return (SubtitleWriter<T>) new SrtSubtitleWriter();
        } else if (type.equals(SubtitleType.ASS)) {
            return (SubtitleWriter<T>) new AssSubtitleWriter();
        } else if (type.equals(SubtitleType.SSA)) {
            return (SubtitleWriter<T>) new AssSubtitleWriter();
        } else if (type.equals(SubtitleType.VTT)) {
            return (SubtitleWriter<T>) new VttSubtitleWriter();
        } else {
            throw new IllegalStateException("Writer has not been implemented for type " + type);
        }
    }
}
