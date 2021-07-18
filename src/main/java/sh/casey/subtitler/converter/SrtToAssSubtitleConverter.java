package sh.casey.subtitler.converter;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.util.AssDefaults;

import java.util.List;

@Slf4j
public class SrtToAssSubtitleConverter implements SubtitleConverter<SrtSubtitleFile, AssSubtitleFile> {

    @Override
    public AssSubtitleFile convert(final SrtSubtitleFile input) {
        log.debug("Converting SRT file to ASS file...");
        final AssSubtitleFile output = AssDefaults.getDefaultAssSubtitleFile();
        final List<SrtSubtitle> lines = input.getSubtitles();
        for (final SrtSubtitle line : lines) {
            final AssDialogue dialogue = AssDefaults.getDefaultDialogue();
            dialogue.setStart(line.getFromTimeForAssFormat());
            dialogue.setEnd(line.getToTimeForAssFormat());

            final Document html = Jsoup.parse(line.getTextForAss());
            dialogue.setText(html.text());
            output.getDialogues().add(dialogue);
        }
        return output;
    }
}
