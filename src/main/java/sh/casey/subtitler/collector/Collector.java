package sh.casey.subtitler.collector;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Collector {

    public void collect(final SubtitleType type, final String outputFile, final List<String> files) {
        log.info("Collecting and reading " + files.size() + " " + type.getExtension() + " files. Outputting to " + outputFile + ".");

        final List<Subtitle> contents = new ArrayList<>();
        final SubtitleReaderFactory factory = new SubtitleReaderFactory();
        final SubtitleReader<? extends SubtitleFile> reader = factory.getInstance(type);
        for (final String file : files) {
            contents.addAll(reader.read(file).getSubtitles());
        }

        final StringBuilder sb = new StringBuilder();
        for (final Subtitle content : contents) {
            sb.append(content.getText()).append("\n");
        }

        FileUtils.writeFile(outputFile, sb.toString());
    }
}
