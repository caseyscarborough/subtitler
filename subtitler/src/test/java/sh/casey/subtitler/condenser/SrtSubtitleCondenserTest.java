package sh.casey.subtitler.condenser;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

import static org.junit.Assert.*;

public class SrtSubtitleCondenserTest {

    SrtSubtitleCondenser condenser;
    @Before
    public void setUp() throws Exception {
        condenser = new SrtSubtitleCondenser();
    }

    @Test
    public void testCondense() {
        SrtSubtitleFile file = new SrtSubtitleFile();
        file.getSubtitles().add(getSubtitle(1, "00:00:00,000", "00:00:01,000", "Hello"));
        file.getSubtitles().add(getSubtitle(2, "00:00:01,000", "00:00:02,000", "Hello"));
        file.getSubtitles().add(getSubtitle(3, "00:00:02,000", "00:00:03,000", "World"));
        SrtSubtitleFile condensed = condenser.condense(file);
        assertEquals(2, condensed.getSubtitles().size());
        final SrtSubtitle first = condensed.getSubtitles().get(0);
        assertEquals(0, (long) first.getStartMilliseconds());
        assertEquals(2000, (long) first.getEndMilliseconds());
    }

    private SrtSubtitle getSubtitle(int number, String start, String end, String text) {
        SrtSubtitle subtitle = new SrtSubtitle();
        subtitle.setNumber(number);
        subtitle.setStart(start);
        subtitle.setEnd(end);
        subtitle.addLine(text);
        return subtitle;
    }
}
