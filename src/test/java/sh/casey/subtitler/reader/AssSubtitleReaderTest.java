package sh.casey.subtitler.reader;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sh.casey.subtitler.util.Timer;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.writer.AssSubtitleWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AssSubtitleReaderTest {

    private static final Logger LOGGER = Logger.getLogger(AssSubtitleReaderTest.class);
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
    public void name() {
        for (String file : files) {
            Timer timer = new Timer();
            timer.start();
            AssSubtitleFile read = reader.read(file);
            writer.write(read, file + ".2");
            AssSubtitleFile read2 = reader.read(file + ".2");
            assertEquals(read.getComments().size(), read2.getComments().size());
            assertEquals(read.getTitle(), read2.getTitle());
            assertEquals(read.getScriptType(), read2.getScriptType());
            assertEquals(read.getWrapStyle(), read2.getWrapStyle());
            assertEquals(read.getPlayResX(), read2.getPlayResX());
            assertEquals(read.getPlayResY(), read2.getPlayResY());
            assertEquals(read.getScaledBorderAndShadow(), read2.getScaledBorderAndShadow());
            assertEquals(read.getVideoAspectRatio(), read2.getVideoAspectRatio());
            assertEquals(read.getVideoZoom(), read2.getVideoZoom());
            assertEquals(read.getVideoPosition(), read2.getVideoPosition());
            assertEquals(read.getStyles().size(), read2.getStyles().size());
            assertEquals(read.getDialogues().size(), read2.getDialogues().size());
            assertFalse(read.getDialogues().isEmpty());
            assertFalse(read.getStyles().isEmpty());
            LOGGER.debug("Took " + timer.stop() + "ms");
        }
        LOGGER.info("Successfully tested " + files.size() + " files.");
    }
}
