package sh.casey.subtitler.dual;

import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DualSubtitleCreatorTest {
    private final SubtitleReaderFactory readerFactory = new SubtitleReaderFactory();
    private final SubtitleReader<SubtitleFile> srtReader = readerFactory.getInstance(SubtitleType.SRT);
    private final SubtitleReader<SubtitleFile> assReader = readerFactory.getInstance(SubtitleType.ASS);
    private DualSubtitleCreator creator;

    @Before
    public void setUp() throws Exception {
        creator = new DualSubtitleCreator();
    }

    @Test
    public void testBasicCreation() {
        SubtitleFile top = srtReader.read("src/test/resources/srt/test1.ja.srt");
        SubtitleFile bottom = srtReader.read("src/test/resources/srt/test1.en.srt");
        SubtitleFile dual = creator.create(top, bottom);
        assertEquals(SubtitleType.ASS, dual.getType());
        assertEquals(top.getSubtitles().size() + bottom.getSubtitles().size(), dual.getSubtitles().size());
    }

    @Test
    public void testKeepingTopStyles() {
        AssSubtitleFile top = (AssSubtitleFile) assReader.read("src/test/resources/ass/test1.ass");
        AssSubtitleFile bottom = (AssSubtitleFile) assReader.read("src/test/resources/ass/test2.ass");
        DualSubtitleConfig config = DualSubtitleConfig.builder()
            .keepTopStyles(true)
            .build();

        final AssSubtitleFile file = (AssSubtitleFile) creator.create(top, bottom, config);
        file.getStyles().stream().filter(s -> s.getName().startsWith("Top_")).forEach(s -> {
            String style = s.getName().substring(4);
            assertTrue(top.getStyles().stream().anyMatch(t -> t.getName().equals(style)));
        });
    }

    @Test
    public void testAlign() {
        AssSubtitleFile top = (AssSubtitleFile) assReader.read("src/test/resources/ass/test2.ass");
        AssSubtitleFile bottom = (AssSubtitleFile) assReader.read("src/test/resources/ass/test1.ass");
        DualSubtitleConfig config = DualSubtitleConfig.builder()
            .align(true)
            .build();
        final AssSubtitleFile file = (AssSubtitleFile) creator.create(top, bottom, config);
        assertTrue(bottom.getStyles().stream().anyMatch(s -> s.getAlignment().equals("8")));
        file.getStyles().stream()
            .filter(s -> s.getName().startsWith("Bottom_"))
            .forEach(s -> assertNotEquals("8", s.getAlignment()));
    }

    @Test
    public void testCustomStyles() {
        AssSubtitleFile top = (AssSubtitleFile) assReader.read("src/test/resources/ass/test1.ass");
        AssSubtitleFile bottom = (AssSubtitleFile) assReader.read("src/test/resources/ass/test2.ass");
        DualSubtitleConfig config = DualSubtitleConfig.builder()
            .topStyle("Fontname", "Kosugi Maru")
            .topStyle("Bold", "true")
            .topStyle("Outline", "5")
            .topStyle("Fontsize", "53")
            .build();

        final AssSubtitleFile file = (AssSubtitleFile) creator.create(top, bottom, config);
        final AssStyle style = file.getStyles()
            .stream()
            .filter(s -> s.getName().equals("Top_Default"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Top_Default style didn't exist!"));

        assertEquals("Kosugi Maru", style.getFontName());
        assertEquals("5", style.getOutline());
        assertEquals("53", style.getFontSize());
        assertTrue(style.isBold());
    }

    // TODO: Add test for copying styles
}
