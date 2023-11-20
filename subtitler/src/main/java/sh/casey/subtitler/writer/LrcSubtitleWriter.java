package sh.casey.subtitler.writer;

import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.LrcSubtitle;
import sh.casey.subtitler.model.LrcSubtitleFile;

import java.io.Writer;
import java.util.Map;

public class LrcSubtitleWriter extends BaseSubtitleWriter<LrcSubtitleFile> {

    public void write(LrcSubtitleFile file, Writer writer) {
        try {
            for (Map.Entry<String, String> entry : file.getMetadata().entrySet()) {
                writer.write("[" + entry.getKey() + ":" + entry.getValue() + "]" + System.lineSeparator());
            }
            for (LrcSubtitle subtitle : file.getSubtitles()) {
                writer.write("[" + subtitle.getStart() + "]" + subtitle.getText() + System.lineSeparator());
            }
            writer.flush();
        } catch (Exception e) {
            throw new SubtitleException("Failed to write LRC file", e);
        }
    }
}
