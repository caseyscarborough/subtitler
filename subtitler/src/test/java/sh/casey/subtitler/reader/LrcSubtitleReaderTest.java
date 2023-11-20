package sh.casey.subtitler.reader;

import org.junit.Assert;
import org.junit.Test;
import sh.casey.subtitler.model.LrcSubtitle;
import sh.casey.subtitler.model.LrcSubtitleFile;

public class LrcSubtitleReaderTest {

    @Test
    public void testRead() {
        LrcSubtitleReader reader = new LrcSubtitleReader();
        final LrcSubtitleFile file = reader.read("src/test/resources/lrc/test1.lrc");
        Assert.assertEquals(87, file.getSubtitles().size());
        for (LrcSubtitle subtitle : file.getSubtitles()) {
            Assert.assertNotNull(subtitle.getText());
            Assert.assertNotNull(subtitle.getStart());
            Assert.assertNotNull(subtitle.getStartMilliseconds());
            Assert.assertTrue(subtitle.getStartMilliseconds() > 0);
        }
    }
}
