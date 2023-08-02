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
    //TODO: Improve this API, it's not very flexible in its current state.
    public void filter(AssSubtitleFile file, String filters, FilterMode mode) {
        Predicate<AssDialogue> dialogue = null;
        Predicate<AssStyle> style = null;
        for (String filter : filters.split(";")) {
            final String[] parts = filter.split("=");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Filter argument was invalid: " + filter);
            }
            String key = parts[0];
            List<String> values = Arrays.stream(parts[1].split(",")).map(String::toLowerCase).collect(Collectors.toList());
            if (key.equals(FilterType.STYLE.getName())) {
                dialogue = d -> values.contains(d.getStyle().toLowerCase(Locale.ROOT));
                style = d -> values.contains(d.getName().toLowerCase(Locale.ROOT));
            } else if (key.equals(FilterType.TEXT.getName())) {
                dialogue = d -> values.contains(d.getText().toLowerCase(Locale.ROOT));
            }
        }

        if (dialogue != null) {
            final int before = file.getDialogues().size();
            Predicate<AssDialogue> predicate = mode == FilterMode.INCLUDE ? dialogue : dialogue.negate();
            file.setDialogues(
                file.getDialogues()
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList())
            );
            log.info("Filtered dialogues from {} to {} dialogues.", before, file.getDialogues().size());
        }

        if (style != null) {
            final int before = file.getStyles().size();
            Predicate<AssStyle> predicate = mode == FilterMode.INCLUDE ? style : style.negate();
            file.setStyles(file.getStyles().stream().filter(predicate).collect(Collectors.toList()));
            log.info("Filtered styles from {} to {} styles.", before, file.getStyles().size());
        }
    }
}
