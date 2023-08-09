package sh.casey.subtitler.filter;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class SrtFiltererTest {

    private String filters;
    private FilterMode mode;
    private int expected;
    private final SrtFilterer filterer = new SrtFilterer();

    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
            {"text=Subtitle,File", FilterMode.OMIT, 8},
            {"text=Subtitle,File", FilterMode.RETAIN, 2},
            {"after=00:00:05.000", FilterMode.OMIT, 3},
            {"after=00:00:05.000", FilterMode.RETAIN, 7},
            {"before=00:00:05.000", FilterMode.OMIT, 8},
            {"before=00:00:05.000", FilterMode.RETAIN, 2},
            {"style=Default", FilterMode.RETAIN, 10},
            {"style=Default", FilterMode.OMIT, 10},
        });
    }

    @Test
    public void testFilter() {
        SrtSubtitleFile file = getSubtitleFile();
        filterer.filter(file, filters, mode);
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
