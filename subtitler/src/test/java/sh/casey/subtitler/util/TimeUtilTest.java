package sh.casey.subtitler.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static sh.casey.subtitler.model.SubtitleType.ASS;
import static sh.casey.subtitler.model.SubtitleType.SRT;

public class TimeUtilTest {

    @Test
    public void testSrtFormatTimeToMilliseconds() {
        assertEquals(35444L, (long) TimeUtil.timeToMilliseconds(SRT, "00:00:35,444"));
    }

    @Test
    public void testAssFormatTimeToMilliseconds() {
        assertEquals(2940L, (long) TimeUtil.timeToMilliseconds(ASS, "0:00:02.94"));
        assertEquals(728000L, (long) TimeUtil.timeToMilliseconds(ASS, "0:12:08.00"));
        assertEquals(3600000L + 600000L + 42000L + 240L, (long) TimeUtil.timeToMilliseconds(ASS, "1:10:42.24"));
    }

    @Test
    public void testAssMillisecondsToTime() {
        assertEquals("0:00:02.94", TimeUtil.millisecondsToTime(ASS, 2940L));
        assertEquals("0:00:00.13", TimeUtil.millisecondsToTime(ASS, 130L));
        assertEquals("0:12:08.00", TimeUtil.millisecondsToTime(ASS, 728000L));
        assertEquals("1:10:42.24", TimeUtil.millisecondsToTime(ASS, 3600000L + 600000L + 42000L + 240L));
    }

    @Test
    public void testSrtMillisecondsToTime() {
        assertEquals("00:00:35,444", TimeUtil.millisecondsToTime(SRT, 35444L));
    }

    @Test
    public void testAssTrim() {
        assertEquals("0:00:02.94", TimeUtil.assTrim("0:00:02.940"));
        assertEquals("0:00:02", TimeUtil.assTrim("0:00:02"));
        assertNull(TimeUtil.assTrim(null));
        assertNull(TimeUtil.assTrim(""));
    }
}
