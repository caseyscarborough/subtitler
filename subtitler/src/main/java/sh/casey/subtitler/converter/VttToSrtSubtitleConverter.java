package sh.casey.subtitler.converter;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.model.VttSubtitle;
import sh.casey.subtitler.model.VttSubtitleFile;

@Slf4j
public class VttToSrtSubtitleConverter implements SubtitleConverter<VttSubtitleFile, SrtSubtitleFile> {
    @Override
    public SrtSubtitleFile convert(VttSubtitleFile input) {
        log.info("Converting VTT file to SRT...");
        SrtSubtitleFile output = new SrtSubtitleFile();

        int number = 1;
        for (VttSubtitle vtt : input.getSubtitles()) {
            SrtSubtitle srt = new SrtSubtitle();
            srt.setStart(vtt.getStart().replace(".", ","));
            srt.setEnd(vtt.getEnd().replace(".", ","));
            srt.setText(vtt.getText());
            srt.setNumber(number++);
            output.getSubtitles().add(srt);
        }

        return output;
    }
}
