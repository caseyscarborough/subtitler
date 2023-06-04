package sh.casey.subtitler.filter;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
class AssFilterer implements Filterer<AssSubtitleFile> {

    @Override
    public void filter(AssSubtitleFile file, FilterType type, String filter) {
        Predicate<AssDialogue> dialogue = null;
        Predicate<AssStyle> style = null;
        if (type == FilterType.STYLE) {
            if (filter.contains(",")) {
                List<String> filters = Arrays.stream(filter.split(",")).map(String::toLowerCase).collect(Collectors.toList());
                dialogue = d -> filters.contains(d.getStyle().toLowerCase(Locale.ROOT));
                style = d -> filters.contains(d.getName().toLowerCase(Locale.ROOT));
            } else {
                dialogue = d -> d.getStyle().equalsIgnoreCase(filter);
                style = d -> d.getName().equalsIgnoreCase(filter);
            }
        }

        if (dialogue == null && style == null) {
            return;
        }

        if (dialogue != null) {
            final int before = file.getDialogues().size();
            file.setDialogues(
                file.getDialogues()
                    .stream()
                    .filter(dialogue)
                    .collect(Collectors.toList())
            );
            log.info("Filtered dialogues from {} to {} dialogues.", before, file.getDialogues().size());
        }

        if (style != null) {
            final int before = file.getStyles().size();
            file.setStyles(
                file.getStyles().stream().filter(style).collect(Collectors.toList())
            );
            log.info("Filtered styles from {} to {} styles.", before, file.getStyles().size());
        }
    }
}
