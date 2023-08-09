package sh.casey.subtitler.filter;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.util.TimeUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static sh.casey.subtitler.filter.FilterMode.OMIT;

@Slf4j
abstract class BaseFilterer<T extends SubtitleFile> implements Filterer<T> {

    abstract void cleanup(T file);

    @Override
    public void filter(T file, String filters, FilterMode mode, int threshold) {
        final String[] parts = filters.split(";");
        Arrays.stream(parts).forEach(filter -> handleFilters(file, filter, mode, threshold));
        cleanup(file);
    }

    private void handleFilters(T file, String filter, FilterMode mode, int threshold) {
        final String[] keyValue = filter.split("=");
        if (keyValue.length != 2) {
            throw new IllegalArgumentException("Filters argument was invalid: " + filter + ". Filters must be in the format of key1=value1,value2;key2=value3,value4");
        }
        final FilterType key = FilterType.findByName(keyValue[0]);
        if (!key.getSupportedTypes().contains(file.getType())) {
            log.warn("Filter \"{}\" is not supported with {} subtitles, ignoring...", key.getName(), file.getType().getExtension());
            return;
        }
        final List<String> values = Arrays.stream(keyValue[1].split(",")).collect(Collectors.toList());
        filter(file, key, values, mode, threshold);
    }

    private void filter(T file, FilterType type, List<String> filters, FilterMode mode, int threshold) {
        if (filters.isEmpty()) {
            return;
        }
        if (type.getMultiplicity().equals(FilterType.Multiplicity.ONE) && filters.size() > 1) {
            log.warn("The multiplicity for the \"{}\" filter is 1. Only the first filter value ({}) will be used.", type.getName(), filters.get(0));
        }
        String verb = mode == OMIT ? "Omitting" : "Retaining";
        Predicate<Subtitle> predicate = null;
        switch (type) {
            case STYLE:
                filterStyles(file, filters, mode, threshold);
                return;
            case TEXT:
                log.info("{} all subtitles where the text is in {}...", verb, filters);
                predicate = subtitle -> filters.contains(subtitle.getText().trim());
                break;
            case AFTER:
                log.info("{} all subtitles where the start time is after {}...", verb, filters.get(0));
                final Long after = TimeUtil.timeToMilliseconds(".", filters.get(0), 1);
                predicate = subtitle -> subtitle.getStartMilliseconds() > after;
                break;
            case BEFORE:
                log.info("{} all subtitles where the end time is before {}...", verb, filters.get(0));
                final Long before = TimeUtil.timeToMilliseconds(".", filters.get(0), 1);
                predicate = subtitle -> subtitle.getEndMilliseconds() < before;
                break;
            default:
                log.warn("Missing implementation for \"{}\" filter for \"{}\" files...", type.getName(), file.getType().getExtension());
        }

        if (predicate == null) {
            log.warn("An unsupported filter attempt was made for {} file, ignoring...", file.getType().getExtension());
            return;
        }

        final Predicate<Subtitle> p = mode == OMIT ? predicate : predicate.negate();
        int before = file.getSubtitles().size();
        long after = file.getSubtitles().stream().filter(p.negate()).count();
        if (before - after > threshold) {
            log.warn("Filtering subtitles by {} would remove {} subtitles, which exceeds the threshold ({}) by {} subtitles. Skipping filtering.", type.getName(), before - after, threshold, (before - after) - threshold);
            return;
        }
        file.getSubtitles().removeIf(p);
        log.debug("Filtered out {} subtitles...", before - after);
    }

    protected void filterStyles(T file, List<String> styles, FilterMode mode, int threshold) {
        // This should be overriden for Aegisub subtitles
    }
}
