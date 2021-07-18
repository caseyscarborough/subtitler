package sh.casey.subtitler.renumberer;

import org.apache.log4j.Logger;
import sh.casey.subtitler.model.AssSubtitleFile;

public class AssSubtitleRenumberer implements SubtitleRenumberer<AssSubtitleFile> {

    private static final Logger LOGGER = Logger.getLogger(AssSubtitleRenumberer.class);

    @Override
    public void renumber(AssSubtitleFile file) {
        LOGGER.debug(".ass subtitles don't have numbers... not renumbering.");
    }
}
