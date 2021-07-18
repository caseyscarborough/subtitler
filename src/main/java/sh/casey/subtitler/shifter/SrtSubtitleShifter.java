package sh.casey.subtitler.shifter;

import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

class SrtSubtitleShifter extends BaseSubtitleShifter<SrtSubtitleFile> {

    @Override
    public SubtitleType getSubtitleType() {
        return SubtitleType.SRT;
    }

    @Override
    public String getDefaultTime() {
        return "00:00:00,000";
    }
}
