package sh.casey.subtitler.model;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubtitleTypeTest {

    @Test
    public void name() {
        DateFormat df = new SimpleDateFormat(SubtitleType.ASS.getTimeFormat());
        String formatted = df.format(new Date());
        System.out.println(formatted);
    }
}
