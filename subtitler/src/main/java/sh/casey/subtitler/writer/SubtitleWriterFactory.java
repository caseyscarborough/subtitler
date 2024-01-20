package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

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
        } else if (type.equals(SubtitleType.LRC)) {
            return (SubtitleWriter<T>) new LrcSubtitleWriter();
        } else {
            throw new IllegalStateException("Writer has not been implemented for type " + type);
        }
    }
}
