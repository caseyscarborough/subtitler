package sh.casey.subtitler.filter;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class AssSubtitleFilterer extends BaseSubtitleFilterer<AssSubtitleFile> {

    @Override
    void cleanup(AssSubtitleFile file) {
        // Nothing to do here
    }

    @Override
    protected void filterStyles(AssSubtitleFile file, List<String> filters, FilterMode mode, int threshold) {
        List<String> styles = filters.stream().map(String::toLowerCase).collect(Collectors.toList());
        Predicate<AssDialogue> dialogue = d -> styles.contains(d.getStyle().toLowerCase(Locale.ROOT));
        Predicate<AssStyle> style = d -> styles.contains(d.getName().toLowerCase(Locale.ROOT));

        String verb = mode == FilterMode.OMIT ? "Omitting" : "Retaining";
        log.info("{} all styles and dialogues where the style is in {}...", verb, filters);
        int before = file.getDialogues().size();
        List<AssDialogue> after = file.getDialogues()
            .stream()
            .filter(mode == FilterMode.RETAIN ? dialogue : dialogue.negate())
            .collect(Collectors.toList());

        if (before - after.size() > threshold) {
            log.warn("Filtering subtitles by style would remove {} subtitles, which exceeds the threshold ({}) by {} subtitles. Skipping filtering.", before - after.size(), threshold, (before - after.size()) - threshold);
            return;
        }
        file.setDialogues(after);
        log.debug("Filtered dialogues from {} to {} dialogues.", before, file.getDialogues().size());

        before = file.getStyles().size();
        file.setStyles(
            file.getStyles()
                .stream()
                .filter(mode == FilterMode.RETAIN ? style : style.negate())
                .collect(Collectors.toList())
        );
        log.debug("Filtered styles from {} to {} styles.", before, file.getStyles().size());
    }
}
