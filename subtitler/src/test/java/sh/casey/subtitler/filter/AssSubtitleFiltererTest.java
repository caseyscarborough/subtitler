package sh.casey.subtitler.filter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.reader.AssSubtitleReader;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AssSubtitleFiltererTest {

    private Map<FilterType, List<String>> filters;
    private AssSubtitleFilterer filterer;

    @Before
    public void setUp() throws Exception {
        filterer = new AssSubtitleFilterer();
        filters = new EnumMap<>(FilterType.class);
    }

    @Test
    public void testIncludingStyle() {
        final AssSubtitleFile file = new AssSubtitleReader().read("src/test/resources/ass/test1.ass");
        assertEquals(328, file.getDialogues().size());
        filters.put(FilterType.STYLE, Arrays.asList("Style1", "Style2"));
        filterer.filter(file, filters, FilterMode.RETAIN, Integer.MAX_VALUE);
        assertEquals(318, file.getDialogues().size());
    }

    @Test
    public void testOmittingStyle() {
        final AssSubtitleFile file = new AssSubtitleReader().read("src/test/resources/ass/test1.ass");
        assertEquals(328, file.getDialogues().size());
        Map<FilterType, List<String>> filters = new EnumMap<>(FilterType.class);
        filters.put(FilterType.STYLE, Arrays.asList("Style1", "Style2"));
        filterer.filter(file, filters, FilterMode.OMIT, Integer.MAX_VALUE);
        assertEquals(10, file.getDialogues().size());
    }
}
