package sh.casey.subtitler.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubtitleUtilsTest {

    @Test
    public void testConvertSrtTimeToAssTime() {
        final String srtTime = "01:02:03,123";
        assertEquals("1:02:03.12", SubtitleUtils.convertSrtTimeToAssTime(srtTime));
    }

    @Test
    public void testConvertAssTimeToSrtTime() {
        final String assTime = "1:02:03.12";
        assertEquals("01:02:03,120", SubtitleUtils.convertAssTimeToSrtTime(assTime));
    }
}
