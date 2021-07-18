package sh.casey.subtitler.condenser;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

@Slf4j
public class SrtSubtitleCondenser implements SubtitleCondenser<SrtSubtitleFile> {

    @Override
    public SrtSubtitleFile condense(final SrtSubtitleFile input) {
        log.info("Condensing subtitle file...");
        final SrtSubtitleFile output = new SrtSubtitleFile();
        SrtSubtitle previous = null;
        int counter = 0;
        for (final SrtSubtitle current : input.getSubtitles()) {
            if (previous != null && previous.getStart().equals(current.getStart()) && previous.getEnd().equals(current.getEnd())) {
                previous.setText(previous.getText().trim() + "\n" + current.getText().trim() + "\n");
            } else {
                current.setNumber(++counter);
                output.getSubtitles().add(current);
            }
            previous = current;
        }
        final int originalLines = input.getSubtitles().size();
        final int outputLines = output.getSubtitles().size();
        final double reducedLines = originalLines - outputLines;
        final double percentage = (reducedLines / originalLines) * 100;
        log.debug("Condensed subtitle from " + originalLines + " lines to " + outputLines + " lines (" + String.format("%.2f", percentage) + "% reduction).");
        return output;
    }
}
