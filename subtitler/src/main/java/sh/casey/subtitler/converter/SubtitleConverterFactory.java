package sh.casey.subtitler.converter;

import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;

public class SubtitleConverterFactory {

    @SuppressWarnings("unchecked")
    public <F extends SubtitleFile, T extends SubtitleFile> SubtitleConverter<F, T> getInstance(final SubtitleType from, final SubtitleType to) {
        if (from == SubtitleType.SRT && to == SubtitleType.ASS) {
            return (SubtitleConverter<F, T>) new SrtToAssSubtitleConverter();
        } else if (from == SubtitleType.ASS && to == SubtitleType.SRT) {
            return (SubtitleConverter<F, T>) new AssToSrtSubtitleConverter();
        } else if (from == SubtitleType.TTML && to == SubtitleType.SRT) {
            return (SubtitleConverter<F, T>) new TtmlToSrtSubtitleConverter();
        } else if (from == SubtitleType.DFXP && to == SubtitleType.ASS) {
            return (SubtitleConverter<F, T>) new DfxpToAssSubtitleConverter();
        } else if (from == SubtitleType.SSA && to == SubtitleType.SRT) {
            return (SubtitleConverter<F, T>) new AssToSrtSubtitleConverter();
        } else if (from == SubtitleType.VTT && to == SubtitleType.SRT) {
            return (SubtitleConverter<F, T>) new VttToSrtSubtitleConverter();
        } else {
            throw new IllegalStateException("Converter has not been implemented for converting from " + from + " to " + to + " subtitle format");
        }
    }
}
