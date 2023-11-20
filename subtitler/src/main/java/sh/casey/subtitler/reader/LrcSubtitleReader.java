package sh.casey.subtitler.reader;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.LrcSubtitle;
import sh.casey.subtitler.model.LrcSubtitleFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LrcSubtitleReader implements SubtitleReader<LrcSubtitleFile> {

    @Override
    public LrcSubtitleFile read(String filename) {
        try (BOMInputStream bis = new BOMInputStream(Files.newInputStream(Paths.get(filename)));
             BufferedReader br = new BufferedReader(new InputStreamReader(bis))) {
            final LrcSubtitleFile lrc = new LrcSubtitleFile();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (StringUtils.isBlank(line)) {
                    continue;
                }

                if (line.matches("\\[\\d{2}:\\d{2}\\.\\d{2}].+")) {
                    LrcSubtitle subtitle = new LrcSubtitle();
                    subtitle.setStart(line.substring(1, line.indexOf("]")));
                    subtitle.setText(line.substring(line.indexOf("]") + 1));
                    lrc.getSubtitles().add(subtitle);
                }
            }

            return lrc;
        } catch (IOException e) {
            throw new SubtitleException("Could not parse LRC file!", e);
        }
    }
}
