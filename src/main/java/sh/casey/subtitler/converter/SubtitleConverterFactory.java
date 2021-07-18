package sh.casey.subtitler.converter;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleConverterFactory {

    public <F extends SubtitleFile, T extends SubtitleFile> SubtitleConverter<F, T> getInstance(SubtitleType from, SubtitleType to) {
        if (from == SubtitleType.SRT && to == SubtitleType.ASS) {
            return (SubtitleConverter<F, T>) new SrtToAssSubtitleConverter();
        } else if (from == SubtitleType.TTML && to == SubtitleType.SRT) {
            return (SubtitleConverter<F, T>) new TtmlToSrtSubtitleConverter();
        } else if (from == SubtitleType.DFXP && to == SubtitleType.ASS) {
            return (SubtitleConverter<F, T>) new DfxpToAssSubtitleConverter();
        } else {
            throw new IllegalStateException("Converter has not been implemented for converting from " + from + " to " + to + " subtitle format");
        }
    }
}
