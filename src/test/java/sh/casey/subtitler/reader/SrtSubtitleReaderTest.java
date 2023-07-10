package sh.casey.subtitler.reader;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.writer.SrtSubtitleWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@Slf4j
public class SrtSubtitleReaderTest {
    private static final List<String> files = new ArrayList<>();
    private SrtSubtitleReader reader;
    private SrtSubtitleWriter writer;

    @BeforeClass
    public static void beforeClass() {
        File[] filesArray = new File("src/test/resources/srt").listFiles();
        for (File file : filesArray) {
            if (file.getName().endsWith("srt")) {
                files.add(file.getAbsolutePath());
            }
        }
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("Cleaning up...");
        File[] filesArray = new File("src/test/resources/srt").listFiles();
        for (File file : filesArray) {
            if (file.getName().endsWith(".2")) {
                System.out.println("Deleting file " + file.getName());
                file.delete();
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        reader = new SrtSubtitleReader();
        writer = new SrtSubtitleWriter();
    }

    @Test
    public void testReadingAndWriting() {
        for (String file : files) {
            SrtSubtitleFile read = reader.read(file);
            writer.write(read, file + ".2");
            SrtSubtitleFile read2 = reader.read(file + ".2");
            assertFalse(read.getSubtitles().isEmpty());
            assertEquals(read.getSubtitles().size(), read2.getSubtitles().size());
        }
        log.info("Successfully tested " + files.size() + " files.");
    }

    @Test
    public void testReadingFileThatHasIncorrectNumbers() {
        SrtSubtitleFile file = reader.read(new File("src/test/resources/tester.srt").getAbsolutePath());
        assertEquals(653, file.getSubtitles().size());
    }
}
