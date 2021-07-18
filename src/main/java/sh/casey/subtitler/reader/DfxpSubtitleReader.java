package sh.casey.subtitler.reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sh.casey.subtitler.util.FileUtils;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.DfxpSubtitleFile;
import sh.casey.subtitler.model.DxfpSubtitle;

public class DfxpSubtitleReader implements SubtitleReader<DfxpSubtitleFile> {

    @Override
    public DfxpSubtitleFile read(String filename) {
        String contents = FileUtils.readFile(filename);
        Document document = Jsoup.parse(contents);
        Elements bodyElements = document.getElementsByTag("body");

        if (bodyElements.isEmpty()) {
            throw new SubtitleException(".dfxp subtitle is in the incorrect format. Could not find body tag.");
        }

        Element body = bodyElements.get(0);
        Element divElement = body.getElementsByTag("div").get(0);
        Elements pElements = divElement.getElementsByTag("p");

        int counter = 1;
        DfxpSubtitleFile file = new DfxpSubtitleFile();
        for (Element p : pElements) {
            String begin = p.attr("begin");
            String end = p.attr("end");
            String region = p.attr("region");
            String id = p.attr("xml:id");
            boolean italic = p.html().contains("<span tts:fontstyle=\"italic\">");
            String text = p.html().replaceAll("<span tts:fontstyle=\"italic\">", "<span style=\"font-style: italic\">");

            DxfpSubtitle subtitle = new DxfpSubtitle();
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
