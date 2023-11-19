package sh.casey.subtitler.reader;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.VttSubtitle;
import sh.casey.subtitler.model.VttSubtitleFile;
import sh.casey.subtitler.util.FileUtils;
import sh.casey.subtitler.writer.VttSubtitleWriter;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VttSubtitleReaderTest {

    private VttSubtitleReader reader;
    private VttSubtitleWriter writer;

    @Before
    public void setUp() throws Exception {
        reader = new VttSubtitleReader();
        writer = new VttSubtitleWriter();
    }

    @Test
    public void testRead() {
        File[] files = new File("src/test/resources/vtt").listFiles();
        assert files != null;
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

    @Test
    public void testWrite() {
        File[] files = new File("src/test/resources/vtt").listFiles();
        assert files != null;
        for (File f : files) {
            final VttSubtitleFile file = reader.read(f.getAbsolutePath());
            final StringWriter sw = new StringWriter();
            writer.write(file, sw);
            final String contents = FileUtils.readFile(f.getAbsolutePath());
            assertEquals(f.getAbsolutePath() + " file did not match!", contents.trim(), sw.toString().trim());
        }
    }
}
