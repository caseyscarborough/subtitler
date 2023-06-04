package sh.casey.subtitler.application.command.completer;

import sh.casey.subtitler.model.SubtitleType;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class FileCompleter implements Iterable<String> {

    @Override
    public Iterator<String> iterator() {
        File[] files = new File(".").listFiles(File::isFile);
        if (files == null) {
            return Collections.emptyIterator();
        }
        return Arrays.stream(files)
            .map(File::getName)
            .filter(f -> Arrays.stream(SubtitleType.values()).anyMatch(t -> f.endsWith(t.getExtension())))
            .iterator();
    }
}
