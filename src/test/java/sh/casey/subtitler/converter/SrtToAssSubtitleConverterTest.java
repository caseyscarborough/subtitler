package sh.casey.subtitler.converter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.reader.SrtSubtitleReader;

public class SrtToAssSubtitleConverterTest {

    private SrtSubtitleReader reader;
    private SrtToAssSubtitleConverter converter;

    @Before
    public void setUp() throws Exception {
        reader = new SrtSubtitleReader();
        converter = new SrtToAssSubtitleConverter();
    }

    @Test
    public void testConvert() {
        // todo: implement
    }
}
