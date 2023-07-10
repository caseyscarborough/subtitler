package sh.casey.subtitler.filter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.reader.AssSubtitleReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AssFiltererTest {

    private AssFilterer filterer;

    @Before
    public void setUp() throws Exception {
        filterer = new AssFilterer();
    }

    @Test
    public void testName() {
        final AssSubtitleFile file = new AssSubtitleReader().read("src/test/resources/ass/[Judas] Enen no Shouboutai (Fire Force) S01E01.en.ass");
        assertEquals(298, file.getDialogues().size());
        filterer.filter(file, FilterType.STYLE.getName(), FilterMode.OMIT);
        assertEquals(228, file.getDialogues().size());
    }
}
