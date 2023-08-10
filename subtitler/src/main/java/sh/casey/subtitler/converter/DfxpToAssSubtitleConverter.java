package sh.casey.subtitler.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.DfxpSubtitleFile;
import sh.casey.subtitler.model.DxfpSubtitle;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.util.AssDefaults;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static sh.casey.subtitler.util.TimeUtil.assTrim;

@Slf4j
public class DfxpToAssSubtitleConverter implements SubtitleConverter<DfxpSubtitleFile, AssSubtitleFile> {
    @Override
    public AssSubtitleFile convert(final DfxpSubtitleFile input) {
        final DateFormat dfxpFormat = new SimpleDateFormat(SubtitleType.DFXP.getTimeFormat());
        final DateFormat assFormat = new SimpleDateFormat(SubtitleType.ASS.getTimeFormat());

        final AssSubtitleFile output = AssDefaults.getDefaultAssSubtitleFile();
        output.getStyles().clear();
        output.getStyles().addAll(AssDefaults.getDefaultStylesWithTopAndBottom());

        for (final DxfpSubtitle subtitle : input.getSubtitles()) {
            try {
                final String start = assTrim(assFormat.format(dfxpFormat.parse(subtitle.getStart())));
                final String end = assTrim(assFormat.format(dfxpFormat.parse(subtitle.getEnd())));

                final AssDialogue dialogue = subtitle.isItalic() ? AssDefaults.getItalicDialogue() : AssDefaults.getDefaultDialogue();
                dialogue.setStart(start);
                dialogue.setEnd(end);
                dialogue.setNumber(subtitle.getNumber());
                final String dialogueText = subtitle.getText()
                    .replaceAll("<br>", "\\\\N")
                    .replaceAll("<br />", "\\\\N")
                    .replaceAll("<br/>", "\\\\N");
                dialogue.setText(StringEscapeUtils.unescapeHtml4(Jsoup.clean(dialogueText, Safelist.none())));
                dialogue.setStyle(subtitle.getRegion().startsWith("top") ? "Top" : "Bottom");
                output.getDialogues().add(dialogue);
            } catch (final ParseException e) {
                log.error("Could not parse time for input subtitle. Start time ({}) and end time ({}). Skipping subtitle.", subtitle.getStart(), subtitle.getEnd());
            }
        }
        return output;
    }
}
