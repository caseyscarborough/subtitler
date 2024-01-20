package sh.casey.subtitler.shifter;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleShifterFactory {

    @SuppressWarnings("unchecked")
    public <T extends SubtitleFile> SubtitleShifter<T> getInstance(final SubtitleType type) {
        if (type.equals(SubtitleType.SRT)) {
            return (SubtitleShifter<T>) new SrtSubtitleShifter();
        } else if (type.equals(SubtitleType.ASS)) {
            return (SubtitleShifter<T>) new AssSubtitleShifter();
        } else if (type.equals(SubtitleType.SSA)) {
            return (SubtitleShifter<T>) new AssSubtitleShifter();
        } else if (type.equals(SubtitleType.VTT)) {
            return (SubtitleShifter<T>) new VttSubtitleShifter();
        } else {
            throw new IllegalStateException(type + " subtitle shifter has not yet been implemented.");
        }
    }
}
