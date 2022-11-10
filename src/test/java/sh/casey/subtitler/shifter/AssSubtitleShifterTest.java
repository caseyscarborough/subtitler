package sh.casey.subtitler.shifter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.reader.AssSubtitleReader;

import java.util.UUID;

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
        final String filePath = "src/test/resources/ass/[Judas] Bleach - S01E04 - 004.en.ass";
        final String newFilePath = "/tmp/" + UUID.randomUUID() + ".ass";
        final AssSubtitleFile file = reader.read(filePath);
        ShiftConfig config = new ShiftConfig("00:12:08,000", null, null, null, 1000, filePath, newFilePath, ShiftMode.FROM_TO);
        shifter.shift(config);
        final AssSubtitleFile newFile = reader.read(newFilePath);
        assertEquals(file.getDialogues().size(), newFile.getDialogues().size());
        final Long start = 728000L;
        for (int i = 0; i < file.getDialogues().size(); i++) {
            AssDialogue a = file.getDialogues().get(i);
            AssDialogue b = newFile.getDialogues().get(i);
            if (a.getStartMilliseconds() > start) {
                assertEquals(a.getStartMilliseconds() + 1000, (long) b.getStartMilliseconds());
            } else {
                assertEquals(a.getStartMilliseconds(), b.getStartMilliseconds());
            }
        }
    }
}
