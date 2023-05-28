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
        for (final SrtSubtitle current : input.getSubtitles()) {
            if (previous != null && previous.getStart().equals(current.getStart()) && previous.getEnd().equals(current.getEnd())) {
                // If two adjacent subtitles have the same start and end times, then we can combine the two
                // into a single subtitle line to reduce the number of unnecessary individual subtitle lines.
                log.debug("Lines {} and {} have the same start and end times - combining them.", previous.getNumber(), current.getNumber());
                previous.setText(previous.getText().trim() + "\n" + current.getText().trim() + "\n");
            } else if (previous != null && previous.getEnd().equals(current.getStart()) && previous.getText().equals(current.getText())) {
                // If two adjacent subtitles have the same contents, then we can combine the two subtitle
                // lines into a single line with the start time of the first subtitle and the end time of
                // the second subtitle, i.e. the following two subtitles:
                //   00:00:01,450 --> 00:00:03,000: "This is a subtitle"
                //   00:00:03,000 --> 00:00:05,420: "This is a subtitle"
                // will become:
                //   00:00:01,450 --> 00:00:05,420: "This is a subtitle"
                log.debug("Lines {} and {} have the same text - concatenating them.", previous.getNumber(), current.getNumber());
                previous.setEnd(current.getEnd());
            } else {
                // If the previous two don't match, then we will keep the current subtitle.
                output.getSubtitles().add(current);
                previous = current;
            }
        }
        // Update the numbers of the subtitles to be sequential.
        int counter = 0;
        for (final SrtSubtitle subtitle : output.getSubtitles()) {
            subtitle.setNumber(++counter);
        }
        final int originalLines = input.getSubtitles().size();
        final int outputLines = output.getSubtitles().size();
        final double reducedLines = originalLines - outputLines;
        final double percentage = (reducedLines / originalLines) * 100;
        log.debug("Condensed subtitle from " + originalLines + " lines to " + outputLines + " lines (" + String.format("%.2f", percentage) + "% reduction).");
        return output;
    }
}
