package sh.casey.subtitler.shifter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.AssSubtitleReader;
import sh.casey.subtitler.util.TimeUtil;

import static org.junit.Assert.assertEquals;

public class AssSubtitleShifterTest {

    AssSubtitleReader reader;
    AssSubtitleShifter shifter;

    @Before
    public void setUp() {
        this.reader = new AssSubtitleReader();
        this.shifter = new AssSubtitleShifter();
    }

    @Test
    public void testShift() {
        final Long after = 728000L;
        final Long before = 780000L;
        final Long shift = 12480L;
        final String filePath = "src/test/resources/ass/test1.ass";
        final AssSubtitleFile file = reader.read(filePath);
        final AssSubtitleFile shifted = reader.read(filePath);
        ShiftConfig config = new ShiftConfig(TimeUtil.millisecondsToTime(SubtitleType.SRT, after), TimeUtil.millisecondsToTime(SubtitleType.SRT, before), null, null, shift, ShiftMode.FROM_TO);
        shifter.shift(shifted, config);
        assertEquals(file.getDialogues().size(), shifted.getDialogues().size());
        for (int i = 0; i < shifted.getDialogues().size(); i++) {
            AssDialogue a = file.getDialogues().get(i);
            AssDialogue b = shifted.getDialogues().get(i);
            if (a.getStartMilliseconds() > after && a.getStartMilliseconds() < before) {
                assertEquals(a.getStartMilliseconds() + shift, (long) b.getStartMilliseconds());
            } else {
                assertEquals(a.getStartMilliseconds(), b.getStartMilliseconds());
            }
        }
    }
}
