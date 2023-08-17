package sh.casey.subtitler.condenser;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssSubtitleFile;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AssSubtitleCondenser implements SubtitleCondenser<AssSubtitleFile> {
    @Override
    public AssSubtitleFile condense(AssSubtitleFile input) {
        final AssSubtitleFile output = new AssSubtitleFile(input);
        output.getSubtitles().clear();
        log.info("Condensing .ass subtitle file...");
        final Set<Integer> removed = new HashSet<>();
        for (int i = 0; i < input.getSubtitles().size(); i++) {
            final AssDialogue current = input.getSubtitles().get(i);
            if (removed.contains(i)) continue;

            // If two adjacent subtitles have the same contents, then we can combine the two subtitle
            // lines into a single line with the start time of the first subtitle and the end time of
            // the second subtitle, i.e. the following two subtitles:
            //   00:00:01,450 --> 00:00:03,000: "This is a subtitle"
            //   00:00:03,000 --> 00:00:05,420: "This is a subtitle"
            // will become:
            //   00:00:01,450 --> 00:00:05,420: "This is a subtitle"
            for (int j = i + 1; j < input.getSubtitles().size(); j++) {
                final AssDialogue compare = input.getSubtitles().get(j);
                if (!removed.contains(j) &&
                    compare.getText() != null &&
                    current.getText() != null &&
                    compare.getLayer().equals(current.getLayer()) &&
                    compare.getStyle().equals(current.getStyle()) &&
                    compare.getStart().equals(current.getEnd()) &&
                    compare.getText().equals(current.getText())) {
                    log.debug("Lines {} and {} have the same text ({}) - concatenating them.", i + 1, j + 1, current.getText());
                    current.setEnd(compare.getEnd());
                    removed.add(j);
                }
            }

            output.getSubtitles().add(current);
        }
        // Update the numbers of the subtitles to be sequential.
        int counter = 0;
        for (final AssDialogue subtitle : output.getSubtitles()) {
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
