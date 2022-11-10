package sh.casey.subtitler.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeUtilTest {

    @Test
    public void testSrtFormatTimeToMilliseconds() {
        assertEquals(35444L, (long) TimeUtil.srtFormatTimeToMilliseconds("00:00:35,444"));
    }

    @Test
    public void testAssFormatTimeToMilliseconds() {
        assertEquals(2940L, (long) TimeUtil.assFormatTimeToMilliseconds("0:00:02.94"));
        assertEquals(728000L, (long) TimeUtil.assFormatTimeToMilliseconds("0:12:08.00"));
        assertEquals(3600000L + 600000L + 42000L + 240L, (long) TimeUtil.assFormatTimeToMilliseconds("1:10:42.24"));
    }

    @Test
    public void testAssMillisecondsToTime() {
        assertEquals("0:00:02.94", TimeUtil.assMillisecondsToTime(2940L));
        assertEquals("0:00:00.13", TimeUtil.assMillisecondsToTime(130L));
        assertEquals("0:12:08.00", TimeUtil.assMillisecondsToTime(728000L));
        assertEquals("1:10:42.24", TimeUtil.assMillisecondsToTime(3600000L + 600000L + 42000L + 240L));
    }
}
