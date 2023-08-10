package sh.casey.subtitler.writer;

import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;

import java.io.IOException;
import java.io.Writer;

public class SrtSubtitleWriter extends BaseSubtitleWriter<SrtSubtitleFile> {
    @Override
    public void write(final SrtSubtitleFile file, final Writer writer) {
        final StringBuilder sb = new StringBuilder();
        for (final SrtSubtitle sub : file.getSubtitles()) {
            sb.append(sub.getNumber())
                .append(System.lineSeparator())
                .append(sub.getStart())
                .append(" --> ")
                .append(sub.getEnd())
                .append(System.lineSeparator())
                .append(sub.getLines().trim())
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        }
        try {
            writer.write(sb.toString().trim());
        } catch (IOException e) {
            throw new SubtitleException("Couldn't write subtitle file", e);
        }
    }
}
