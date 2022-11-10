package sh.casey.subtitler.shifter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SrtSubtitleReader;

import static org.junit.Assert.assertEquals;

public class SrtSubtitleShifterTest {

    SrtSubtitleReader reader;
    SubtitleShifter<SubtitleFile> shifter;

    @Before
    public void setUp() throws Exception {
        reader = new SrtSubtitleReader();
        shifter = new SubtitleShifterFactory().getInstance(SubtitleType.SRT);
    }

    @Test
    public void testShiftingWithAfter() {
        final SubtitleFile file = reader.read("src/test/resources/tester.srt");
        ShiftConfig config = ShiftConfig.builder()
            .ms(10000)
            .after("00:01:34,000")
            .build();
        shifter.shift(file, config);
        assertEquals("00:01:33,680", file.getSubtitles().get(0).getStart());
        assertEquals("00:01:45,800", file.getSubtitles().get(1).getStart());
        assertEquals("00:01:47,520", file.getSubtitles().get(2).getStart());
    }
}
