package sh.casey.subtitler.cli.command.completer;

import sh.casey.subtitler.shifter.ShiftMode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ShiftModeCompleter implements Iterable<String> {

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(ShiftMode.values())
            .map(ShiftMode::toString)
            .collect(Collectors.toList())
            .iterator();
    }
}
