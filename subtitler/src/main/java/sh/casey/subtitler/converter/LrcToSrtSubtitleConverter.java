package sh.casey.subtitler.converter;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.LrcSubtitle;
import sh.casey.subtitler.model.LrcSubtitleFile;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.util.TimeUtil;

@Slf4j
public class LrcToSrtSubtitleConverter implements SubtitleConverter<LrcSubtitleFile, SrtSubtitleFile> {

    /**
     * This is used as the time for the last subtitle.
     * Since .lrc files only have the start time, we can only infer the end time
     * for each .srt subtitle from the start time of the next subtitle, which
     * we cannot do for the last subtitle in the file.
     */
    private static final long BUFFER_MS = 5000;

    @Override
    public SrtSubtitleFile convert(LrcSubtitleFile input) {
        log.debug("Converting .lrc file to .srt file...");
        SrtSubtitleFile output = new SrtSubtitleFile();
        for (int i = 0; i < input.getSubtitles().size(); i++) {
            LrcSubtitle curr = input.getSubtitles().get(i);
            LrcSubtitle next = i < input.getSubtitles().size() - 1 ? input.getSubtitles().get(i + 1) : null;
            SrtSubtitle out = new SrtSubtitle();
            out.setText(curr.getText());
            out.setNumber(i + 1);
            out.setStart(TimeUtil.millisecondsToTime(SubtitleType.SRT, curr.getStartMilliseconds()));
            out.setEnd(TimeUtil.millisecondsToTime(SubtitleType.SRT, next != null ? next.getStartMilliseconds() : curr.getStartMilliseconds() + BUFFER_MS));
            output.getSubtitles().add(out);
        }
        return output;
    }
}
