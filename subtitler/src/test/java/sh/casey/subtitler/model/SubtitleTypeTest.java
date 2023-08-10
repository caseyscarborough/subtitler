package sh.casey.subtitler.model;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SubtitleTypeTest {

    @Test
    public void testTimeFormat() {
        DateFormat df = new SimpleDateFormat(SubtitleType.ASS.getTimeFormat());
        String formatted = df.format(new Date());
        System.out.println(formatted);
    }

    @Test
    public void testFind() {
        final SubtitleType type = SubtitleType.find("ass");
        assertNotNull(type);
        assertEquals(SubtitleType.ASS, type);
    }
}
