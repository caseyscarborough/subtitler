package sh.casey.subtitler.converter;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.util.AssDefaults;
import sh.casey.subtitler.util.SubtitleUtils;

import java.util.List;

@Slf4j
public class SrtToAssSubtitleConverter implements SubtitleConverter<SrtSubtitleFile, AssSubtitleFile> {

    @Override
    public AssSubtitleFile convert(final SrtSubtitleFile input) {
        log.debug("Converting SRT file to ASS file...");
        final AssSubtitleFile output = AssDefaults.getDefaultAssSubtitleFile();
        final List<SrtSubtitle> subtitles = input.getSubtitles();
        // TODO: Test/implement color matching
        // Pattern color = Pattern.compile("<font color=\"(.*)\">");
        for (final SrtSubtitle subtitle : subtitles) {
            final AssDialogue dialogue = AssDefaults.getDefaultDialogue();
            dialogue.setStart(SubtitleUtils.convertSrtTimeToAssTime(subtitle.getStart()));
            dialogue.setEnd(SubtitleUtils.convertSrtTimeToAssTime(subtitle.getEnd()));

            final String lines = String.join("\n", subtitle.getLines())
                .replace("<i>", "{\\i1}")
                .replace("</i>", "{\\i0}")
                .replace("<b>", "{\\b1}")
                .replace("</b>", "{\\b0}")
                .replace("<u>", "{\\u1}}")
                .replace("</u>", "{\\u0}")
                .replace("\n", "\\N");

            final Document html = Jsoup.parse(lines);
            dialogue.setText(html.text());
            output.getDialogues().add(dialogue);
        }
        return output;
    }
}
