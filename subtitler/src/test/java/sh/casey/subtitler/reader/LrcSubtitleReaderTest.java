package sh.casey.subtitler.reader;

import org.junit.Test;
import sh.casey.subtitler.model.LrcSubtitle;
import sh.casey.subtitler.model.LrcSubtitleFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LrcSubtitleReaderTest {

    @Test
    public void testRead() {
        LrcSubtitleReader reader = new LrcSubtitleReader();
        final LrcSubtitleFile file = reader.read("src/test/resources/lrc/test1.lrc");
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
    }
}
