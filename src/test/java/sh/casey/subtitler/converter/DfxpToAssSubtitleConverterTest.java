package sh.casey.subtitler.converter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.DfxpSubtitleFile;
import sh.casey.subtitler.reader.DfxpSubtitleReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DfxpToAssSubtitleConverterTest {

    private DfxpSubtitleReader reader;
    private DfxpToAssSubtitleConverter converter;

    @Before
    public void setUp() throws Exception {
        reader = new DfxpSubtitleReader();
        converter = new DfxpToAssSubtitleConverter();
    }

    @Test
    public void testConvert() {
        DfxpSubtitleFile input = reader.read("src/test/resources/dfxp/test1.dfxp");
        AssSubtitleFile output = converter.convert(input);

        boolean containsN = false;
        for (AssDialogue dialogue : output.getDialogues()) {
            System.out.println(dialogue);
            if (dialogue.getText().contains("\\N")) {
                containsN = true;
            }
        }

        assertTrue(containsN);
        assertEquals(455, output.getDialogues().size());
    }
}
