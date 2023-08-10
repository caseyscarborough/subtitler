package sh.casey.subtitler.filter;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static sh.casey.subtitler.filter.FilterType.AFTER;
import static sh.casey.subtitler.filter.FilterType.BEFORE;
import static sh.casey.subtitler.filter.FilterType.MATCHES;
import static sh.casey.subtitler.filter.FilterType.STYLE;
import static sh.casey.subtitler.filter.FilterType.TEXT;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class SrtFiltererTest {

    private final SrtFilterer filterer = new SrtFilterer();
    private Map<FilterType, List<String>> filters;
    private FilterMode mode;
    private int expected;
    private int threshold;

    @Parameterized.Parameters
    public static Object[][] parameters() {
        return new Object[][]{
            {getFilters(TEXT, asList("Subtitle", "File")), FilterMode.OMIT, 9, Integer.MAX_VALUE},
            {getFilters(TEXT, asList("Subtitle", "File")), FilterMode.RETAIN, 2, Integer.MAX_VALUE},
            {getFilters(AFTER, asList("00:00:05.000")), FilterMode.OMIT, 3, Integer.MAX_VALUE},
            {getFilters(AFTER, asList("00:00:05.000")), FilterMode.RETAIN, 8, Integer.MAX_VALUE},
            {getFilters(BEFORE, asList("00:00:05.000")), FilterMode.OMIT, 9, Integer.MAX_VALUE},
            {getFilters(BEFORE, asList("00:00:05.000")), FilterMode.RETAIN, 2, Integer.MAX_VALUE},
            {getFilters(STYLE, asList("Default")), FilterMode.RETAIN, 11, Integer.MAX_VALUE},
            {getFilters(STYLE, asList("Default")), FilterMode.OMIT, 11, Integer.MAX_VALUE},
            {getFilters(MATCHES, asList("[A-z]+")), FilterMode.OMIT, 1, Integer.MAX_VALUE},
            {getFilters(MATCHES, asList(".*\\d.*")), FilterMode.OMIT, 10, Integer.MAX_VALUE},
            {getFilters(MATCHES, asList("[A-z]+")), FilterMode.RETAIN, 10, Integer.MAX_VALUE},
            {getFilters(MATCHES, asList(".*\\d.*")), FilterMode.RETAIN, 1, Integer.MAX_VALUE},
            {getFilters(TEXT, asList("Subtitle", "File")), FilterMode.OMIT, 11, 1},
            {getFilters(TEXT, asList("Subtitle", "File")), FilterMode.RETAIN, 11, 1},
            {getFilters(AFTER, asList("00:00:05.000")), FilterMode.OMIT, 11, 1},
            {getFilters(AFTER, asList("00:00:05.000")), FilterMode.RETAIN, 11, 1},
            {getFilters(BEFORE, asList("00:00:05.000")), FilterMode.OMIT, 11, 1},
            {getFilters(BEFORE, asList("00:00:05.000")), FilterMode.RETAIN, 11, 1},
            {getFilters(MATCHES, asList("[A-z]")), FilterMode.RETAIN, 11, 1},
        };
    }

    private static Map<FilterType, List<String>> getFilters(FilterType type, List<String> filters) {
        Map<FilterType, List<String>> output = new EnumMap<>(FilterType.class);
        output.put(type, filters);
        return output;
    }

    @Test
    public void testFilter() {
        SrtSubtitleFile file = getSubtitleFile();
        filterer.filter(file, filters, mode, threshold);
        assertEquals(expected, file.getSubtitles().size());
    }

    private SrtSubtitleFile getSubtitleFile() {
        SrtSubtitleFile file = new SrtSubtitleFile();
        file.getSubtitles().add(getSubtitle("This", "00:00:01,000", "00:00:02,000"));
        file.getSubtitles().add(getSubtitle("Is", "00:00:03,000", "00:00:04,000"));
        file.getSubtitles().add(getSubtitle("A", "00:00:05,000", "00:00:06,000"));
        file.getSubtitles().add(getSubtitle("Test", "00:00:07,000", "00:00:08,000"));
        file.getSubtitles().add(getSubtitle("Subtitle", "00:00:09,000", "00:00:10,000"));
        file.getSubtitles().add(getSubtitle("File", "00:00:11,000", "00:00:12,000"));
        file.getSubtitles().add(getSubtitle("With", "00:00:13,000", "00:00:14,000"));
        file.getSubtitles().add(getSubtitle("Some", "00:00:15,000", "00:00:16,000"));
        file.getSubtitles().add(getSubtitle("Basic", "00:00:17,000", "00:00:18,000"));
        file.getSubtitles().add(getSubtitle("Text", "00:00:19,000", "00:00:20,000"));
        file.getSubtitles().add(getSubtitle("This subtitle has 10 numbers in it 12341234", "00:00:21,000", "00:00:22,000"));
        return file;
    }

    private SrtSubtitle getSubtitle(String text, String start, String end) {
        SrtSubtitle subtitle = new SrtSubtitle();
        subtitle.setText(text);
        subtitle.setStart(start);
        subtitle.setEnd(end);
        return subtitle;
    }
}
