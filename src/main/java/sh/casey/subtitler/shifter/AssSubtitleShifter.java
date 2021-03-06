package sh.casey.subtitler.shifter;

import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

class AssSubtitleShifter extends BaseSubtitleShifter<AssSubtitleFile> {

    @Override
    public SubtitleType getSubtitleType() {
        return SubtitleType.ASS;
    }

    @Override
    public String getDefaultTime() {
        return "0:00:00.00";
    }
}
