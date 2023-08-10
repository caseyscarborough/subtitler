package sh.casey.subtitler.cli.command.completer;

import sh.casey.subtitler.dual.StyleConfig;

import java.util.Arrays;
import java.util.Iterator;

public class StyleConfigCompleter implements Iterable<String> {
    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(StyleConfig.values()).map(StyleConfig::getName).iterator();
    }
}
