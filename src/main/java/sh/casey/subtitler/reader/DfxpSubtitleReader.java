package sh.casey.subtitler.reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.DfxpSubtitleFile;
import sh.casey.subtitler.model.DxfpSubtitle;
import sh.casey.subtitler.util.FileUtils;

public class DfxpSubtitleReader implements SubtitleReader<DfxpSubtitleFile> {

    @Override
    public DfxpSubtitleFile read(final String filename) {
        final String contents = FileUtils.readFile(filename);
        final Document document = Jsoup.parse(contents);
        final Elements bodyElements = document.getElementsByTag("body");

        if (bodyElements.isEmpty()) {
            throw new SubtitleException(".dfxp subtitle is in the incorrect format. Could not find body tag.");
        }

        final Element body = bodyElements.get(0);
        final Element divElement = body.getElementsByTag("div").get(0);
        final Elements pElements = divElement.getElementsByTag("p");

        int counter = 1;
        final DfxpSubtitleFile file = new DfxpSubtitleFile();
        for (final Element p : pElements) {
            final String begin = p.attr("begin");
            final String end = p.attr("end");
            final String region = p.attr("region");
            final String id = p.attr("xml:id");
            final boolean italic = p.html().contains("<span tts:fontstyle=\"italic\">");
            final String text = p.html().replaceAll("<span tts:fontstyle=\"italic\">", "<span style=\"font-style: italic\">");

            final DxfpSubtitle subtitle = new DxfpSubtitle();
            subtitle.setId(id);
            subtitle.setText(text);
            subtitle.setStart(begin);
            subtitle.setEnd(end);
            subtitle.setNumber(counter++);
            subtitle.setRegion(region);
            file.getSubtitles().add(subtitle);
        }
        return file;
    }
}
