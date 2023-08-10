package sh.casey.subtitler.converter;

import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.model.TtmlSubtitle;
import sh.casey.subtitler.model.TtmlSubtitleFile;

import java.util.List;

public class TtmlToSrtSubtitleConverter implements SubtitleConverter<TtmlSubtitleFile, SrtSubtitleFile> {
    @Override
    public SrtSubtitleFile convert(final TtmlSubtitleFile input) {
        final SrtSubtitleFile output = new SrtSubtitleFile();
        final List<TtmlSubtitle> subtitles = input.getSubtitles();
        for (final TtmlSubtitle ttml : subtitles) {
            final SrtSubtitle srt = new SrtSubtitle();
            srt.setStart(ttml.getStart().replace(".", ","));
            srt.setEnd(ttml.getEnd().replace(".", ","));
            srt.setNumber(ttml.getNumber());
            srt.setText(ttml.getText() + "\n");
            output.getSubtitles().add(srt);
        }
        return output;
    }
}
