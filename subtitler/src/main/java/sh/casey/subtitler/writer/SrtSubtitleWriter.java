package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

public class SrtSubtitleWriter implements SubtitleWriter<SrtSubtitleFile> {
    @Override
    public void write(final SrtSubtitleFile file, final String outputPath) {
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

        final String output = sb.toString().trim();
        FileUtils.writeFile(outputPath, output);
    }
}
