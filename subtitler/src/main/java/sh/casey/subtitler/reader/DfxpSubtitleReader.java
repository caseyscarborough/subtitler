package sh.casey.subtitler.reader;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.DfxpSubtitleFile;
import sh.casey.subtitler.model.DxfpSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;


public class DfxpSubtitleReader implements SubtitleReader<DfxpSubtitleFile> {

    @Override
    @SneakyThrows
    public DfxpSubtitleFile read(final InputStream inputStream, final String filePath) {

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, Charset.defaultCharset());

        return parse(writer.toString(), filePath);

    }

    @Override
    public DfxpSubtitleFile read(final String filePath) {
        final String contents = FileUtils.readFile(filePath);
        return parse(contents, filePath);

    }

    private DfxpSubtitleFile parse(String contents, String filePath) {

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
        file.setPath(filePath);
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
