package sh.casey.subtitler.converter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.reader.AssSubtitleReader;
import sh.casey.subtitler.reader.SrtSubtitleReader;
import sh.casey.subtitler.writer.SrtSubtitleWriter;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AssToSrtSubtitleConverterTest {

    private AssSubtitleReader reader;
    private AssToSrtSubtitleConverter converter;

    @Before
    public void setUp() throws Exception {
        this.reader = new AssSubtitleReader();
        this.converter = new AssToSrtSubtitleConverter();
    }

    @Test
    public void testConvert1() {
        AssSubtitleFile file = reader.read("src/test/resources/ass/test2.ass");
        final SrtSubtitleFile converted = converter.convert(file);
        final SrtSubtitle first = converted.getSubtitles().get(0);
        assertEquals("<font color=\"#fdfdfd\"><i>Let's go grab them up! The Dragon Balls!</i></font>", first.getText());
        assertEquals("00:00:10,610", first.getStart());
        assertEquals("00:00:14,010", first.getEnd());
    }

    @Test
    public void testConvertAssColorToHex() {
        Map<String, String> colors = new HashMap<>();
        colors.put("FF", "ff0000");
        colors.put("D301", "01d300");
        colors.put("123456", "563412");

        for (Map.Entry<String, String> entry : colors.entrySet()) {
            assertEquals(entry.getValue(), converter.convertAssColorToHex(entry.getKey()));
        }
    }
}
