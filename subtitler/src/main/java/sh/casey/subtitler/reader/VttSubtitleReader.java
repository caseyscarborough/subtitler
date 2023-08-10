package sh.casey.subtitler.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.VttSubtitle;
import sh.casey.subtitler.model.VttSubtitleFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class VttSubtitleReader implements SubtitleReader<VttSubtitleFile> {

    private static final String WEBVTT_HEADER = "WEBVTT";

    public static void main(String[] args) {
        VttSubtitleReader reader = new VttSubtitleReader();
        VttSubtitleFile file = reader.read("/Volumes/Audio/Music/Kenshi Yonezu/月を見ていた (2023)/01. 米津玄師 - 月を見ていた Kenshi Yonezu - Tsuki Wo Miteita ⧸ Moongazing-video.ja.vtt");
        file.getSubtitles().stream().forEach(System.out::println);
    }

    @Override
    public VttSubtitleFile read(final String filename) {
        // TODO: Implement this method
        // https://www.w3.org/TR/webvtt1/
        // https://sites.utexas.edu/cofawebteam/requirements/ada/captions/webvtt-files-for-video-subtitling/
        log.info("Reading subtitle file " + filename);
        final VttSubtitleFile file = new VttSubtitleFile();
        file.setPath(filename);
        int lineCounter = 0;
        int number = 1;
        try (BOMInputStream bis = new BOMInputStream(new FileInputStream(filename));
             BufferedReader br = new BufferedReader(new InputStreamReader(bis))) {

            String line = br.readLine().trim();
            lineCounter++;
            if (!line.equals(WEBVTT_HEADER)) {
                throw new IllegalStateException("Expected first line of file " + filename + " to be WEBVTT");
            }

            while (line != null) {
                if (line.contains("-->")) {
                    // we reached the start of a cue
                    final String[] parts = line.split(" --> ");
                    if (parts.length != 2) {
                        throw new IllegalStateException("Invalid cue at line " + lineCounter + " for file " + filename);
                    }

                    final String start = parts[0];
                    final String end = parts[1];

                    VttSubtitle subtitle = new VttSubtitle();
                    subtitle.setStart(start);
                    subtitle.setEnd(end);
                    subtitle.setNumber(number++);

                    while (true) {
                        line = br.readLine();
                        lineCounter++;
                        if (line == null || line.contains("-->")) {
                            break;
                        }
                        if (!line.isEmpty()) {
                            subtitle.addLine(line.trim());
                        }
                    }
                    file.getSubtitles().add(subtitle);
                } else {
                    line = br.readLine();
                    lineCounter++;
                }
            }

            log.debug("Read {} subtitles from file {} with {} lines", file.getSubtitles().size(), filename, lineCounter);
            return file;
        } catch (final FileNotFoundException e) {
            throw new SubtitleException("Could not find file " + filename, e);
        } catch (final ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | IOException e) {
            throw new SubtitleException("An error occurred reading lines at line " + lineCounter + " for file " + filename + ". The file might be in an invalid format", e);
        }
    }
}
