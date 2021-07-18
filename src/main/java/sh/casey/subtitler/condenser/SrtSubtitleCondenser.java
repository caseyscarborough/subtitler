package sh.casey.subtitler.condenser;

import org.apache.log4j.Logger;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

public class SrtSubtitleCondenser implements SubtitleCondenser<SrtSubtitleFile> {

    private static final Logger LOGGER = Logger.getLogger(SrtSubtitleCondenser.class);

    @Override
    public SrtSubtitleFile condense(SrtSubtitleFile input) {
        LOGGER.info("Condensing subtitle file...");
        SrtSubtitleFile output = new SrtSubtitleFile();
        SrtSubtitle previous = null;
        int counter = 0;
        for (SrtSubtitle current : input.getSubtitles()) {
            if (previous != null && previous.getStart().equals(current.getStart()) && previous.getEnd().equals(current.getEnd())) {
                previous.setText(previous.getText().trim() + "\n" + current.getText().trim() + "\n");
            } else {
                current.setNumber(++counter);
                output.getSubtitles().add(current);
            }
            previous = current;
        }
        int originalLines = input.getSubtitles().size();
        int outputLines = output.getSubtitles().size();
        double reducedLines = originalLines - outputLines;
        double percentage = (reducedLines / originalLines) * 100;
        LOGGER.debug("Condensed subtitle from " + originalLines + " lines to " + outputLines + " lines (" + String.format("%.2f", percentage) + "% reduction).");
        return output;
    }
}
