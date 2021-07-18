package sh.casey.subtitler.shifter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.reader.SrtSubtitleReader;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SrtSubtitleShifterTest {

    SrtSubtitleReader reader;
    SrtSubtitleShifter shifter;

    @Before
    public void setUp() throws Exception {
        reader = new SrtSubtitleReader();
        shifter = new SrtSubtitleShifter();
    }

    @Test
    public void testShiftingWithAfter() {
        File file = new File("src/test/resources/tester.srt");
        try {
            ShiftConfig config = ShiftConfig.builder()
                .input(file.getAbsolutePath())
                .output(file.getAbsolutePath() + ".2")
                .ms(10000)
                .after("00:01:34,000")
                .build();
            shifter.shift(config);
            SrtSubtitleFile srt2 = reader.read(file.getAbsolutePath() + ".2");
            assertEquals("00:01:33,680", srt2.getSubtitles().get(0).getStart());
            assertEquals("00:01:45,800", srt2.getSubtitles().get(1).getStart());
            assertEquals("00:01:47,520", srt2.getSubtitles().get(2).getStart());
        } finally {
            new File(file.getAbsolutePath() + ".2").delete();
        }
    }
}
