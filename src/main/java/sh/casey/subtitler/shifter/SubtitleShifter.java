package sh.casey.subtitler.shifter;

import sh.casey.subtitler.model.SubtitleFile;

public interface SubtitleShifter<T extends SubtitleFile> {

    void shift(ShiftConfig config);
}
