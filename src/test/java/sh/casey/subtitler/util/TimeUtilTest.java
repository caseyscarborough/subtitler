package sh.casey.subtitler.util;

import junit.framework.TestCase;

public class TimeUtilTest extends TestCase {

    public void testSrtFormatTimeToMilliseconds() {
        assertEquals(35444L, (long) TimeUtil.srtFormatTimeToMilliseconds("00:00:35,444"));
    }

    public void testAssFormatTimeToMilliseconds() {
        assertEquals(2940L, (long) TimeUtil.assFormatTimeToMilliseconds("0:00:02.94"));
        assertEquals(3600000L + 600000L + 42000L + 240L, (long) TimeUtil.assFormatTimeToMilliseconds("1:10:42.24"));
    }
}
