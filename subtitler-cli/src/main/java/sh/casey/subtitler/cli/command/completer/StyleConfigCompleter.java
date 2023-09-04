package sh.casey.subtitler.cli.command.completer;

import sh.casey.subtitler.model.AssStyle;

import java.util.Iterator;

public class StyleConfigCompleter implements Iterable<String> {
    @Override
    public Iterator<String> iterator() {
        return AssStyle.ATTRIBUTES.iterator();
    }
}
