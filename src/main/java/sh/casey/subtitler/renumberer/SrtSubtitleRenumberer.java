package sh.casey.subtitler.renumberer;

import org.apache.log4j.Logger;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

public class SrtSubtitleRenumberer implements SubtitleRenumberer<SrtSubtitleFile> {

    private static final Logger LOGGER = Logger.getLogger(SrtSubtitleRenumberer.class);

    @Override
    public void renumber(SrtSubtitleFile file) {
        LOGGER.debug("Renumbering SRT subtitle file...");
        int counter = 0;
        for (SrtSubtitle subtitle : file.getSubtitles()) {
            subtitle.setNumber(++counter);
        }
    }
}
