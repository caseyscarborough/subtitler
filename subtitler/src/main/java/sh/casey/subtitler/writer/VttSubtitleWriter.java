package sh.casey.subtitler.writer;

import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.VttSubtitle;
import sh.casey.subtitler.model.VttSubtitleFile;

import java.io.Writer;

public class VttSubtitleWriter extends BaseSubtitleWriter<VttSubtitleFile> {
    @Override
    public void write(VttSubtitleFile file, Writer writer) {
        final StringBuilder sb = new StringBuilder();
        sb.append("WEBVTT").append(System.lineSeparator());
        for (final String key : file.getMetadata().keySet()) {
            sb.append(key).append(": ").append(file.getMetadata().get(key)).append(System.lineSeparator());
        }
        for (final VttSubtitle sub : file.getSubtitles()) {
            sb.append(System.lineSeparator())
                .append(sub.getStart())
                .append(" --> ")
                .append(sub.getEnd())
                .append(System.lineSeparator())
                .append(sub.getLines().trim())
                .append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        try {
            writer.write(sb.toString().trim());
            writer.flush();
        } catch (Exception e) {
            throw new SubtitleException("Couldn't write subtitle file", e);
        }
    }
}
