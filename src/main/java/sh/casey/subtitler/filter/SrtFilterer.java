package sh.casey.subtitler.filter;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.renumberer.SrtSubtitleRenumberer;

@Slf4j
class SrtFilterer implements Filterer<SrtSubtitleFile> {

    private final SrtSubtitleRenumberer renumberer = new SrtSubtitleRenumberer();

    @Override
    public void filter(SrtSubtitleFile file, String filters, FilterMode mode) {
        log.info("Removing subtitles containing any of the following text: {}", filters);
        int count = file.getSubtitles().size();
        // For SRT files, we can only filter on text.
        for (String filter : filters.split(",")) {
            file.getSubtitles().removeIf(subtitle -> subtitle.getText().trim().equals(filter));
        }
        int removed = count - file.getSubtitles().size();
        renumberer.renumber(file);
        log.info("Removed {} subtitles.", removed);
    }
}
