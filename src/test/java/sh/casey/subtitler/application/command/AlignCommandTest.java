package sh.casey.subtitler.application.command;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.shifter.ShiftMode;
import sh.casey.subtitler.util.FileUtils;

import static org.junit.Assert.assertEquals;

public class AlignCommandTest {

    private static final String OUTPUT_FILE = "src/test/resources/align/output.srt";
    private static final String REFERENCE_FILE = "src/test/resources/align/reference.srt";
    private AlignCommand command;
    private SubtitleReaderFactory factory;
    private SubtitleReader<SubtitleFile> reader;

    @Before
    public void setUp() throws Exception {
        command = new AlignCommand();
        command.setInput("src/test/resources/align/input.srt");
        command.setReferenceFilename(REFERENCE_FILE);
        command.setThreshold(720);
        command.setMode(ShiftMode.FROM_TO);
        command.setOutput(OUTPUT_FILE);

        factory = new SubtitleReaderFactory();
        reader = factory.getInstance(SubtitleType.SRT);
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteFile(OUTPUT_FILE);
    }

    @Test
    public void testAlign() {
        command.run();

        final SubtitleFile output = reader.read(OUTPUT_FILE);
        final SubtitleFile reference = reader.read(REFERENCE_FILE);

        // This assertion checks that when two subtitles are close together
        // and "match", that the input subtitle has the same start/end times
        // as the reference file.
        checkSubtitle(output, "リュカ？", "01:40:33,110","01:40:34,070");

        // This assertion checks that when a long input subtitle overlaps
        // multiple smaller reference subtitles, the last input subtitle
        // will have the same start time as the first reference subtitle
        // and the same end time as the last reference subtitle.
        checkSubtitle(output, "“忘れる”は難しいです", "00:31:14,950", "00:31:18,160");
        checkSubtitle(output, "（ヴァイオレット）その先生は\n皆さんの先生なのですよね？", "01:17:09,040", "01:17:13,090");

        // This assertion checks that when a long reference subtitle overlaps
        // multiple smaller subtitles in the input file, the last input subtitle
        // will have the same end time as the reference subtitle.
        checkSubtitle(output, "その３つは\nなるべく感情から排除します", null, "00:41:52,720");
        checkSubtitle(output, "ライデンの兵士たちに\n殺されちゃったんだよね？", null, "01:10:35,890");
        checkSubtitle(output, "ひいおばあちゃんが亡くなったあと\n届いた手紙だね", null, "00:03:21,110");

        // TODO: Add test for 27:20-28:00 in [Beatrice-Raws] Evangelion 2.0 You Can (Not) Advance [BDRip 1920x1080 HEVC TrueHD].ja.srt
        // not sure what's causing the issue but a block of about 40 seconds were completely unaligned
    }

    private void checkSubtitle(SubtitleFile file, String text, String startTime, String endTime) {
        Subtitle subtitle = file.getSubtitles()
            .stream()
            .filter(s -> s.getText().equals(text))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Could not find expected subtitle for test"));

        if (startTime != null) {
            assertEquals(startTime, subtitle.getStart());
        }
        if (endTime != null) {
            assertEquals(endTime, subtitle.getEnd());
        }
    }
}
