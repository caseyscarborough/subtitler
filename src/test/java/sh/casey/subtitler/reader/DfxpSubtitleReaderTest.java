package sh.casey.subtitler.reader;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.DfxpSubtitleFile;

import static org.junit.Assert.assertEquals;

public class DfxpSubtitleReaderTest {

    private DfxpSubtitleReader reader;

    @Before
    public void setUp() throws Exception {
        reader = new DfxpSubtitleReader();
    }

    @Test
    public void testRead() {
        DfxpSubtitleFile file = reader.read("src/test/resources/dfxp/[CostcoSubs] Terrace House - Boys x Girls Next Door - 01.dfxp");
        assertEquals(455, file.getSubtitles().size());
    }
}
