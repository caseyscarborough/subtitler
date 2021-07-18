package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.SrtSubtitle;
import sh.casey.subtitler.model.SrtSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

public class SrtSubtitleWriter implements SubtitleWriter<SrtSubtitleFile> {
    @Override
    public void write(SrtSubtitleFile file, String outputPath) {
        StringBuilder sb = new StringBuilder();
        for (SrtSubtitle sub : file.getSubtitles()) {
            sb.append(sub.getNumber())
                .append("\n")
                .append(sub.getStart())
                .append(" --> ")
                .append(sub.getEnd())
                .append("\n")
                .append(sub.getLines())
                .append("\n");
        }

        String output = sb.toString().trim();
        FileUtils.writeFile(outputPath, output);
    }
}
