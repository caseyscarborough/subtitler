package sh.casey.subtitler.filter;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.renumberer.SrtSubtitleRenumberer;

@Slf4j
public class SrtSubtitleFilterer extends BaseSubtitleFilterer<SrtSubtitleFile> {

    private final SrtSubtitleRenumberer renumberer = new SrtSubtitleRenumberer();


    @Override
    void cleanup(SrtSubtitleFile file) {
        renumberer.renumber(file);
    }
}
