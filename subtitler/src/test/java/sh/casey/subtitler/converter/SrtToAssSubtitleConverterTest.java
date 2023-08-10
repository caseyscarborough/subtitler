package sh.casey.subtitler.converter;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.reader.SrtSubtitleReader;

import static org.junit.Assert.assertEquals;

public class SrtToAssSubtitleConverterTest {

    private SrtSubtitleReader reader;
    private SrtToAssSubtitleConverter srtToAssConverter;
    private AssToSrtSubtitleConverter assToSrtConverter;

    @Before
    public void setUp() throws Exception {
        reader = new SrtSubtitleReader();
        srtToAssConverter = new SrtToAssSubtitleConverter();
        assToSrtConverter = new AssToSrtSubtitleConverter();
    }

    @Test
    public void testConvert() {
        final SrtSubtitleFile srt = reader.read("src/test/resources/srt/test1.srt");
        final AssSubtitleFile ass = srtToAssConverter.convert(srt);
        assertEquals(srt.getSubtitles().size(), ass.getDialogues().size());

        final SrtSubtitleFile end = assToSrtConverter.convert(ass);
        assertEquals(srt.getSubtitles().size(), end.getSubtitles().size());
        for (int i = 0; i < srt.getSubtitles().size(); i++) {
            Subtitle a = srt.getSubtitles().get(i);
            Subtitle b = end.getSubtitles().get(i);
            assertEquals(a.getText(), b.getText());
        }
    }
}
