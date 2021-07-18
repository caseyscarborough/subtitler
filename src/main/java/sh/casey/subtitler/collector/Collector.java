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

    public void collect(SubtitleType type, String outputFile, List<String> files) {
        log.info("Collecting and reading " + files.size() + " " + type.getExtension() + " files. Outputting to " + outputFile + ".");

        List<Subtitle> contents = new ArrayList<>();
        SubtitleReaderFactory factory = new SubtitleReaderFactory();
        SubtitleReader<? extends SubtitleFile> reader = factory.getInstance(type);
        for (String file : files) {
            contents.addAll(reader.read(file).getSubtitles());
        }

        StringBuilder sb = new StringBuilder();
        for (Subtitle content : contents) {
            sb.append(content.getText()).append("\n");
        }

        FileUtils.writeFile(outputFile, sb.toString());
    }
}
