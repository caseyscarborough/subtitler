package sh.casey.subtitler.renumberer;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.AssSubtitleFile;

@Slf4j
public class AssSubtitleRenumberer implements SubtitleRenumberer<AssSubtitleFile> {

    @Override
    public void renumber(final AssSubtitleFile file) {
        log.debug(".ass subtitles don't have numbers... not renumbering.");
    }
}
