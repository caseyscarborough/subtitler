package sh.casey.subtitler.reader;

import org.junit.Test;
import sh.casey.subtitler.model.LrcSubtitle;
import sh.casey.subtitler.model.LrcSubtitleFile;
import sh.casey.subtitler.util.FileUtils;
import sh.casey.subtitler.writer.LrcSubtitleWriter;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LrcSubtitleReaderTest {

    private final LrcSubtitleReader reader = new LrcSubtitleReader();
    private final LrcSubtitleWriter writer = new LrcSubtitleWriter();

    @Test
    public void testRead() {
        final String path = "src/test/resources/lrc/test1.lrc";
        final LrcSubtitleFile file = reader.read(path);
        assertEquals(87, file.getSubtitles().size());
        int previous = 0;
        for (LrcSubtitle subtitle : file.getSubtitles()) {
            assertNotNull(subtitle.getText());
            assertNotNull(subtitle.getStart());
            assertNotNull(subtitle.getStartMilliseconds());
            assertNotNull(subtitle.getNumber());
            assertTrue(subtitle.getNumber() > previous);
            assertTrue(subtitle.getStartMilliseconds() > 0);
            previous = subtitle.getNumber();
        }

        StringWriter sw = new StringWriter();
        writer.write(file, sw);
        assertEquals(FileUtils.readFile(path).trim(), sw.toString().trim());
    }
}
