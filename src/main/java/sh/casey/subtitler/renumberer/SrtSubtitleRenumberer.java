package sh.casey.subtitler.renumberer;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

@Slf4j
public class SrtSubtitleRenumberer implements SubtitleRenumberer<SrtSubtitleFile> {

    @Override
    public void renumber(final SrtSubtitleFile file, int start) {
        log.debug("Renumbering SRT subtitle file...");
        int counter = start;
        for (final SrtSubtitle subtitle : file.getSubtitles()) {
            subtitle.setNumber(counter++);
        }
    }
}
