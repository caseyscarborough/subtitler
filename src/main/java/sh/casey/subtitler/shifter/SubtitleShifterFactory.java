package sh.casey.subtitler.shifter;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleShifterFactory {

    public SubtitleShifter<? extends SubtitleFile> getInstance(final SubtitleType type) {
        if (type.equals(SubtitleType.SRT)) {
            return new SrtSubtitleShifter();
        } else if (type.equals(SubtitleType.ASS)) {
            return new AssSubtitleShifter();
        } else {
            throw new IllegalStateException(type + " subtitle shifter has not yet been implemented.");
        }
    }
}
