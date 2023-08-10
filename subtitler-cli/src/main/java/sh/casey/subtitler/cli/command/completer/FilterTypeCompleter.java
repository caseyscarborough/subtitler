package sh.casey.subtitler.cli.command.completer;

import sh.casey.subtitler.filter.FilterType;

import java.util.Arrays;
import java.util.Iterator;

public class FilterTypeCompleter implements Iterable<String> {
    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(FilterType.values()).map(f -> "\n  - " + f).iterator();
    }
}
