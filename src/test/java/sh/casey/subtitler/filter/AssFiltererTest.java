package sh.casey.subtitler.filter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.reader.AssSubtitleReader;

import static org.junit.Assert.assertEquals;

public class AssFiltererTest {

    private AssFilterer filterer;

    @Before
    public void setUp() throws Exception {
        filterer = new AssFilterer();
    }

    @Test
    public void testIncludingStyle() {
        final AssSubtitleFile file = new AssSubtitleReader().read("src/test/resources/ass/test1.ass");
        assertEquals(328, file.getDialogues().size());
        filterer.filter(file, FilterType.STYLE.getName() + "=Style1,Style2", FilterMode.RETAIN);
        assertEquals(318, file.getDialogues().size());
    }

    @Test
    public void testOmittingStyle() {
        final AssSubtitleFile file = new AssSubtitleReader().read("src/test/resources/ass/test1.ass");
        assertEquals(328, file.getDialogues().size());
        filterer.filter(file, FilterType.STYLE.getName() + "=Style1,Style2", FilterMode.OMIT);
        assertEquals(10, file.getDialogues().size());
    }
}
