package sh.casey.subtitler.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class SrtSubtitleReader implements SubtitleReader<SrtSubtitleFile> {

    @Override
    public SrtSubtitleFile read(final String filename) {
        log.info("Reading subtitle file " + filename);
        final SrtSubtitleFile file = new SrtSubtitleFile();
        file.setPath(filename);
        int lineCounter = 0;
        try (BOMInputStream bis = new BOMInputStream(new FileInputStream(filename));
             BufferedReader br = new BufferedReader(new InputStreamReader(bis))) {

            String line = br.readLine();
            boolean first = true;
            while (line != null) {
                line = line.trim();

                if (StringUtils.isNotBlank(line)) {
                    // Once we find a line, the first thing should be a subtitle number.
                    if (first) {
                        first = false;
                    }
                    final int number = Integer.parseInt(line.trim());
                    lineCounter++;
                    final String timings = br.readLine().trim();
                    final String[] split = timings.split(" --> ");
                    final String start = split[0];
                    final String end = split[1];

                    final SrtSubtitle subtitle = new SrtSubtitle();
                    subtitle.setNumber(number);
                    subtitle.setStart(start);
                    subtitle.setEnd(end);

                    boolean foundBlank = false;
                    while (line != null) {
                        lineCounter++;
                        line = br.readLine();

                        // Only break when we find a number by itself on a line after
                        // the previous line was blank. This allows subtitles to have
                        // multiple lines where one line might be blank, as well as
                        // doesn't rely on the "numbers" in the file to be in order.
                        if (StringUtils.isNotBlank(line) && foundBlank && StringUtils.isNumeric(line)) {
                            break;
                        }

                        foundBlank = StringUtils.isBlank(line);
                        if (line != null) {
                            subtitle.addLine(line);
                        }
                    }
                    file.getSubtitles().add(subtitle);
                } else {
                    lineCounter++;
                    line = br.readLine();
                }
            }
            log.debug("Read " + lineCounter + " lines from file " + filename);
        } catch (final FileNotFoundException e) {
            throw new SubtitleException("Could not find file " + filename, e);
        } catch (final ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | IOException e) {
            throw new SubtitleException("An error occurred reading lines at line " + lineCounter + " for file " + filename + ". The file might be in an invalid format", e);
        }

        log.debug("Found " + file.getSubtitles().size() + " subtitles.");
        return file;
    }
}
