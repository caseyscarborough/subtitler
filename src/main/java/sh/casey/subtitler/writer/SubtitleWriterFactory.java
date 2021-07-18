package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleWriterFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleWriter<T> getInstance(SubtitleType type) {
        if (type.equals(SubtitleType.SRT)) {
            return (SubtitleWriter<T>) new SrtSubtitleWriter();
        } else if (type.equals(SubtitleType.ASS)) {
            return (SubtitleWriter<T>) new AssSubtitleWriter();
        } else {
            throw new IllegalStateException("Writer has not been implemented for type " + type);
        }
    }
}
