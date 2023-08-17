package sh.casey.subtitler.condenser;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssScriptInfo;
import sh.casey.subtitler.model.AssStyleVersion;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.reader.AssSubtitleReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AssSubtitleCondenserTest {

    AssSubtitleCondenser condenser;

    @Before
    public void setUp() throws Exception {
        condenser = new AssSubtitleCondenser();
    }

    @Test
    public void testCondense1() {
        AssSubtitleFile file = getSubtitleFile();
        file.getSubtitles().add(getSubtitle(1, "00:00:00.00", "00:00:01.00", "Hello"));
        file.getSubtitles().add(getSubtitle(2, "00:00:01.00", "00:00:02.00", "Hello"));
        file.getSubtitles().add(getSubtitle(3, "00:00:02.00", "00:00:03.00", "World"));
        AssSubtitleFile condensed = condenser.condense(file);
        assertEquals(2, condensed.getSubtitles().size());
        final AssDialogue first = condensed.getSubtitles().get(0);
        assertEquals(0, (long) first.getStartMilliseconds());
        assertEquals(2000, (long) first.getEndMilliseconds());
    }

    @Test
    public void testCondense2() {
        AssSubtitleReader reader = new AssSubtitleReader();
        final AssSubtitleFile file = reader.read("src/test/resources/ass/test1.ass");
        final AssSubtitleFile condensed = condenser.condense(file);
    }

    private AssSubtitleFile getSubtitleFile() {
        AssSubtitleFile file = new AssSubtitleFile();
        file.setScriptInfo(new AssScriptInfo());
        file.setStyles(new ArrayList<>());
        file.setEventsFormatOrder(new ArrayList<>());
        file.setStylesFormatOrder(new ArrayList<>());
        file.setStyleVersion(AssStyleVersion.V4PLUS);
        file.setDialogues(new ArrayList<>());
        return file;
    }

    private AssDialogue getSubtitle(int number, String start, String end, String text) {
        AssDialogue subtitle = new AssDialogue();
        subtitle.setStart(start);
        subtitle.setEnd(end);
        subtitle.setText(text);
        subtitle.setStyle("Default");
        subtitle.setLayer("0");
        subtitle.setNumber(number);
        return subtitle;
    }
}
