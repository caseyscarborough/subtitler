package sh.casey.subtitler.application.command.completer;

import sh.casey.subtitler.model.SubtitleType;

import java.util.Arrays;
import java.util.Iterator;

public class SubtitleTypeCompleter implements Iterable<String> {

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(SubtitleType.values())
            .map(SubtitleType::name)
            .iterator();
    }
}
