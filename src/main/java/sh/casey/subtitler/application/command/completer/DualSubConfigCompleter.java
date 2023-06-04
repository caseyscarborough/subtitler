package sh.casey.subtitler.application.command.completer;

import sh.casey.subtitler.application.command.config.DualSubConfig;

import java.util.Arrays;
import java.util.Iterator;

public class DualSubConfigCompleter implements Iterable<String> {
    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(DualSubConfig.values()).map(DualSubConfig::getName).iterator();
    }
}
