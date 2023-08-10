package sh.casey.subtitler.converter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.DfxpSubtitleFile;
import sh.casey.subtitler.reader.DfxpSubtitleReader;

import static org.junit.Assert.assertEquals;

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
        DfxpSubtitleFile input = reader.read("src/test/resources/dfxp/test1.en.dfxp");
        AssSubtitleFile output = converter.convert(input);
        assertEquals(23, output.getDialogues().size());
    }
}
