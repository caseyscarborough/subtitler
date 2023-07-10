package sh.casey.subtitler.condenser;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SrtSubtitleCondenser implements SubtitleCondenser<SrtSubtitleFile> {

    @Override
    public SrtSubtitleFile condense(final SrtSubtitleFile input) {
        log.info("Condensing subtitle file...");
        final SrtSubtitleFile output = new SrtSubtitleFile();
        final Set<Integer> removed = new HashSet<>();
        for (int i = 0; i < input.getSubtitles().size(); i++) {
            final SrtSubtitle current = input.getSubtitles().get(i);
            if (removed.contains(current.getNumber())) continue;

            // If two adjacent subtitles have the same contents, then we can combine the two subtitle
            // lines into a single line with the start time of the first subtitle and the end time of
            // the second subtitle, i.e. the following two subtitles:
            //   00:00:01,450 --> 00:00:03,000: "This is a subtitle"
            //   00:00:03,000 --> 00:00:05,420: "This is a subtitle"
            // will become:
            //   00:00:01,450 --> 00:00:05,420: "This is a subtitle"
            for (int j = i + 1; j < input.getSubtitles().size(); j++) {
                final SrtSubtitle compare = input.getSubtitles().get(j);
                if (!removed.contains(compare.getNumber()) &&
                    compare.getStart().equals(current.getEnd()) &&
                    compare.getText().equals(current.getText())) {
                    log.debug("Lines {} and {} have the same text - concatenating them.", current.getNumber(), compare.getNumber());
                    current.setEnd(compare.getEnd());
                    removed.add(compare.getNumber());
                }
            }

            output.getSubtitles().add(current);
        }
        // Update the numbers of the subtitles to be sequential.
        int counter = 0;
        for (final SrtSubtitle subtitle : output.getSubtitles()) {
            subtitle.setNumber(++counter);
        }
        final int originalLines = input.getSubtitles().size();
        final int outputLines = output.getSubtitles().size();
        final int reducedLines = originalLines - outputLines;
        final double percentage = ((double) reducedLines / originalLines) * 100;
        log.debug("Condensed subtitle from " + originalLines + " lines to " + outputLines + " lines (removing " + reducedLines + " lines, a " + String.format("%.2f", percentage) + "% reduction).");
        return output;
    }
}
