package sh.casey.subtitler.reader;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleReaderFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleReader<T> getInstance(final SubtitleType type) {
        if (type.equals(SubtitleType.ASS)) {
            return (SubtitleReader<T>) new AssSubtitleReader();
        } else if (type.equals(SubtitleType.SRT)) {
            return (SubtitleReader<T>) new SrtSubtitleReader();
        } else if (type.equals(SubtitleType.TTML)) {
            return (SubtitleReader<T>) new TtmlSubtitleReader();
        } else if (type.equals(SubtitleType.DFXP)) {
            return (SubtitleReader<T>) new DfxpSubtitleReader();
        } else if (type.equals(SubtitleType.SSA)) {
            return (SubtitleReader<T>) new AssSubtitleReader();
        } else if (type.equals(SubtitleType.VTT)) {
            return (SubtitleReader<T>) new VttSubtitleReader();
        } else if (type.equals(SubtitleType.LRC)) {
            return (SubtitleReader<T>) new LrcSubtitleReader();
        } else {
            throw new IllegalStateException("No reader found for type " + type);
        }
    }
}
