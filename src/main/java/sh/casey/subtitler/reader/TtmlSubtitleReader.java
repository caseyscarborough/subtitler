package sh.casey.subtitler.reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sh.casey.subtitler.model.TtmlSubtitle;
import sh.casey.subtitler.model.TtmlSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

public class TtmlSubtitleReader implements SubtitleReader<TtmlSubtitleFile> {

    @Override
    public TtmlSubtitleFile read(String filename) {
        String html = FileUtils.readFile(filename);
        Document document = Jsoup.parse(html);
        TtmlSubtitleFile file = new TtmlSubtitleFile();
        Elements ps = document.getElementsByTag("p");
        int counter = 0;
        for (Element p : ps) {
            TtmlSubtitle subtitle = new TtmlSubtitle();
            subtitle.setNumber(++counter);
            subtitle.setStart(p.attr("begin"));
            subtitle.setEnd(p.attr("end"));
            subtitle.setText(p.text());
            file.getSubtitles().add(subtitle);
        }
        return file;
    }
}
