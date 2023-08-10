package sh.casey.subtitler.reader;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sh.casey.subtitler.model.AssScriptInfo;
import sh.casey.subtitler.util.Timer;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.writer.AssSubtitleWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
public class AssSubtitleReaderTest {

    private static final List<String> files = new ArrayList<>();
    private AssSubtitleReader reader;
    private AssSubtitleWriter writer;

    @BeforeClass
    public static void beforeClass() {
        File[] filesArray = new File("src/test/resources/ass").listFiles();
        for (File file : filesArray) {
            if (file.getName().endsWith("ass")) {
                files.add(file.getAbsolutePath());
            }
        }
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("Cleaning up...");
        File[] filesArray = new File("src/test/resources/ass").listFiles();
        for (File file : filesArray) {
            if (file.getName().endsWith(".2")) {
                System.out.println("Deleting file " + file.getName());
                file.delete();
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        reader = new AssSubtitleReader();
        writer = new AssSubtitleWriter();
    }

    @Test
    public void test() {
        final AssSubtitleFile file = reader.read("src/test/resources/ass/[ShadyCrab] Fullmetal Alchemist Brotherhood - S01E02v3 [BD][1080p][Hi10][Dual][0379C76B].ja.ass");
        assertTrue(file.getDialogues().size() > 0);
    }

    @Test
    public void name() {
        for (String file : files) {
            Timer timer = new Timer();
            timer.start();
            AssSubtitleFile read1 = reader.read(file);
            writer.write(read1, file + ".2");
            AssSubtitleFile read2 = reader.read(file + ".2");
            AssScriptInfo info1 = read1.getScriptInfo();
            AssScriptInfo info2 = read2.getScriptInfo();
            assertEquals(info1.getComments().size(), info2.getComments().size());
            assertEquals(info1.getTitle(), info2.getTitle());
            assertEquals(info1.getScriptType(), info2.getScriptType());
            assertEquals(info1.getWrapStyle(), info2.getWrapStyle());
            assertEquals(info1.getPlayResX(), info2.getPlayResX());
            assertEquals(info1.getPlayResY(), info2.getPlayResY());
            assertEquals(info1.getScaledBorderAndShadow(), info2.getScaledBorderAndShadow());
            assertEquals(info1.getVideoAspectRatio(), info2.getVideoAspectRatio());
            assertEquals(info1.getVideoZoom(), info2.getVideoZoom());
            assertEquals(info1.getVideoPosition(), info2.getVideoPosition());
            assertEquals(read1.getStyles().size(), read2.getStyles().size());
            assertEquals(read1.getDialogues().size(), read2.getDialogues().size());
            assertFalse(read1.getDialogues().isEmpty());
            assertFalse(read1.getStyles().isEmpty());
            log.debug("Took " + timer.stop() + "ms");
        }
        log.info("Successfully tested " + files.size() + " files.");
    }
}
