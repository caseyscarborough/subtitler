package sh.casey.subtitler.writer;

import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.LrcSubtitle;
import sh.casey.subtitler.model.LrcSubtitleFile;

import java.io.Writer;

public class LrcSubtitleWriter extends BaseSubtitleWriter<LrcSubtitleFile> {

    public void write(LrcSubtitleFile file, Writer writer) {
        try {
            for (LrcSubtitle subtitle : file.getSubtitles()) {
                writer.write("[" + subtitle.getStart() + "]" + subtitle.getText() + System.lineSeparator());
            }
            writer.flush();
        } catch (Exception e) {
            throw new SubtitleException("Failed to write LRC file", e);
        }
    }
}
