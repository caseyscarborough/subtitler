package sh.casey.subtitler.reader;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.VttSubtitle;
import sh.casey.subtitler.model.VttSubtitleFile;

import java.io.File;

import static org.junit.Assert.assertNotNull;

public class VttSubtitleReaderTest {

    private VttSubtitleReader reader;

    @Before
    public void setUp() throws Exception {
        reader = new VttSubtitleReader();
    }

    @Test
    public void test() {
        File[] files = new File("src/test/resources/vtt").listFiles();
        for (File f : files) {
            final VttSubtitleFile file = reader.read(f.getAbsolutePath());
            for (VttSubtitle subtitle : file.getSubtitles()) {
                assertNotNull(subtitle.getEndMilliseconds());
                assertNotNull(subtitle.getStartMilliseconds());
                assertNotNull(subtitle.getText());
                assertNotNull(subtitle.getStart());
                assertNotNull(subtitle.getEnd());
            }

        }

    }
}
