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
            if (!line.startsWith(WEBVTT_HEADER)) {
                throw new IllegalStateException("Expected first line of file " + filename + " to be WEBVTT");
            }

            boolean started = false;
            while (line != null) {
                if (line.contains("-->")) {
                    started = true;
                    // we reached the start of a cue
                    final String[] parts = line.split(" --> ");
                    if (parts.length != 2) {
                        throw new IllegalStateException("Invalid cue at line " + lineCounter + " for file " + filename);
                    }

                    VttSubtitle subtitle = new VttSubtitle();
                    final String start = parts[0];
                    String end = parts[1];

                    // Handle styles in VTT subtitles, for example:
                    // 00:00:04.212 --> 00:00:06.131 position:50.00%,middle align:middle size:80.00% line:79.33%
                    final String[] split = end.split(" ");
                    if (split.length > 1) {
                        end = split[0];
                        for (int i = 1; i < split.length; i++) {
                            final String style = split[i];
                            final String[] pair = style.split(":");
                            if (pair.length != 2) {
                                throw new IllegalStateException("Invalid style '" + style + "' at line " + lineCounter + " for file " + filename);
                            }
                            subtitle.getStyles().put(pair[0], pair[1]);
                        }
                    }

                    subtitle.setStart(start);
                    subtitle.setEnd(end);
                    subtitle.setNumber(number++);

                    while (true) {
                        line = br.readLine();
                        lineCounter++;
                        if (line == null || line.isEmpty() || line.contains("-->") || line.startsWith("NOTE ")) {
                            break;
                        }
                        subtitle.addLine(line.trim());
                    }
                    file.getSubtitles().add(subtitle);
                } else {
                    // TODO: Implement NOTE and STYLE blocks
                    // TODO: Remove this metadata as this isn't part of the WebVTT spec
                    if (!started && line.contains(":")) {
                        int index = line.indexOf(":");
                        String key = line.substring(0, index);
                        String value = line.substring(index + 1).trim();
                        file.getMetadata().put(key, value);
                    }

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
