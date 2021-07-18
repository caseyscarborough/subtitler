package sh.casey.subtitler.reader;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.config.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SrtSubtitleReader implements SubtitleReader<SrtSubtitleFile> {

    private static final Logger LOGGER = Logger.getLogger(SrtSubtitleReader.class);

    @Override
    public SrtSubtitleFile read(String filename) {
        LOGGER.info("Reading subtitle file " + filename);
        SrtSubtitleFile file = new SrtSubtitleFile();
        BufferedReader br = null;
        int lineCounter = 0;
        try {
            br = new BufferedReader(new FileReader(filename));

            String line = br.readLine();
            boolean first = true;
            while (line != null) {
                line = line.trim();

                if (StringUtils.isNotBlank(line)) {
                    // Once we find a line, the first thing should be a subtitle number.
                    if (first) {
                        line = line.replace(Constants.BYTE_ORDER_MARK, "");
                        first = false;
                    }
                    int number = Integer.parseInt(line.trim());
                    int nextNumber = number + 1;
                    lineCounter++;
                    String timings = br.readLine().trim();
                    String[] split = timings.split(" --> ");
                    String start = split[0];
                    String end = split[1];

                    SrtSubtitle subtitle = new SrtSubtitle();
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
            LOGGER.debug("Read " + lineCounter + " lines from file " + filename);
        } catch (FileNotFoundException e) {
            throw new SubtitleException("Could not find file " + filename, e);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | IOException e) {
            throw new SubtitleException("An error occurred reading lines at line " + lineCounter + " for file " + filename + ". The file might be in an invalid format", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                LOGGER.warn("Couldn't close stream for file " + filename, e);
            }
        }

        LOGGER.debug("Found " + file.getSubtitles().size() + " subtitles.");
        return file;
    }
}
