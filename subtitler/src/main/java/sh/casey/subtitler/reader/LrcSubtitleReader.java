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
            int number = 1;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (StringUtils.isBlank(line)) {
                    continue;
                }

                if (!line.startsWith("[")) {
                    throw new SubtitleException("Invalid LRC file at line number: " + lineNumber);
                }

                if (line.matches("\\[\\d{2}:\\d{2}\\.\\d{2}].+")) {
                    LrcSubtitle subtitle = new LrcSubtitle();
                    subtitle.setStart(line.substring(1, line.indexOf("]")));
                    subtitle.setText(line.substring(line.indexOf("]") + 1));
                    subtitle.setNumber(number++);
                    lrc.getSubtitles().add(subtitle);
                } else if (line.endsWith("]")) {
                    String info = line.substring(1, line.length() - 1);
                    String key = info.substring(0, line.indexOf(":") - 1);
                    String value = info.substring(line.indexOf(":"));
                    lrc.getMetadata().put(key, value);
                } else {
                    throw new SubtitleException("Invalid LRC file at line number: " + lineNumber);
                }
            }

            return lrc;
        } catch (IOException e) {
            throw new SubtitleException("Could not parse LRC file!", e);
        }
    }
}
