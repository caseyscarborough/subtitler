package sh.casey.subtitler.application.command.completer;

import sh.casey.subtitler.application.command.config.StyleConfig;

import java.util.Arrays;
import java.util.Iterator;

public class StyleConfigCompleter implements Iterable<String> {
    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(StyleConfig.values()).map(StyleConfig::getName).iterator();
    }
}
