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
    public AssSubtitleFile convert(SrtSubtitleFile input) {
        log.debug("Converting SRT file to ASS file...");
        AssSubtitleFile output = AssDefaults.getDefaultAssSubtitleFile();
        List<SrtSubtitle> lines = input.getSubtitles();
        for (SrtSubtitle line : lines) {
            AssDialogue dialogue = AssDefaults.getDefaultDialogue();
            dialogue.setStart(line.getFromTimeForAssFormat());
            dialogue.setEnd(line.getToTimeForAssFormat());

            Document html = Jsoup.parse(line.getTextForAss());
            dialogue.setText(html.text());
            output.getDialogues().add(dialogue);
        }
        return output;
    }
}
