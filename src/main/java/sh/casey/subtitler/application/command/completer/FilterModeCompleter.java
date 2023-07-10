package sh.casey.subtitler.application.command.completer;

import sh.casey.subtitler.filter.FilterMode;

import java.util.Arrays;
import java.util.Iterator;

public class FilterModeCompleter implements Iterable<String> {
    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(FilterMode.values()).map(FilterMode::toString).iterator();
    }
}
