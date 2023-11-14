package sh.casey.subtitler.reader;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sh.casey.subtitler.model.TtmlSubtitle;
import sh.casey.subtitler.model.TtmlSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;


public class TtmlSubtitleReader implements SubtitleReader<TtmlSubtitleFile> {

    @Override
    @SneakyThrows
    public TtmlSubtitleFile read(final InputStream inputStream, final String filePath) {

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, Charset.defaultCharset());

        return parse(writer.toString(), filePath);

    }


    @Override
    public TtmlSubtitleFile read(final String filePath) {
        final String html = FileUtils.readFile(filePath);
        return parse(html, filePath);
    }


    private TtmlSubtitleFile parse(final String html, final String filePath) {

        final Document document = Jsoup.parse(html);
        final TtmlSubtitleFile file = new TtmlSubtitleFile();
        file.setPath(filePath);
        final Elements ps = document.getElementsByTag("p");
        int counter = 0;
        for (final Element p : ps) {
            final TtmlSubtitle subtitle = new TtmlSubtitle();
            subtitle.setNumber(++counter);
            subtitle.setStart(p.attr("begin"));
            subtitle.setEnd(p.attr("end"));
            subtitle.setText(p.text());
            file.getSubtitles().add(subtitle);
        }
        return file;

    }

}
