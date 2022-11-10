package sh.casey.subtitler.writer;

import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssStyleVersion;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

public class AssSubtitleWriter implements SubtitleWriter<AssSubtitleFile> {

    private static final String CRLF = System.lineSeparator();

    @Override
    public void write(final AssSubtitleFile file, final String outputPath) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Script Info]").append(CRLF);

        for (final String comment : file.getScriptInfo().getComments()) {
            sb.append(comment).append(CRLF);
        }

        for (String attribute : file.getScriptInfo().getAttributes()) {
            sb.append(attribute).append(CRLF);
        }

        if (!file.getStyles().isEmpty()) {
            sb.append(CRLF)
                .append(file.getStyleVersion() == AssStyleVersion.V4PLUS ? "[V4+ Styles]" : "[V4 Styles]")
                .append(CRLF)
                .append("Format: ")
                .append(String.join(", ", file.getStylesFormatOrder()))
                .append(CRLF);

            for (final AssStyle style : file.getStyles()) {
                sb.append("Style: ");

                boolean first = true;
                for (final String key : file.getStylesFormatOrder()) {
                    if (!first) {
                        sb.append(",");
                    }
                    final String value = style.getValue(key);
                    sb.append(value != null ? value : "");
                    first = false;
                }
                sb.append(CRLF);
            }
        }

        if (!file.getDialogues().isEmpty()) {
            sb.append(CRLF)
                .append("[Events]")
                .append(CRLF)
                .append("Format: ")
                .append(String.join(", ", file.getEventsFormatOrder()))
                .append(CRLF);

            for (final AssDialogue dialogue : file.getDialogues()) {
                sb.append(dialogue.isComment() ? "Comment: " : "Dialogue: ");
                boolean first = true;
                for (final String key : file.getEventsFormatOrder()) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(dialogue.getValue(key));
                    first = false;
                }
                sb.append(CRLF);
            }
        }

        FileUtils.writeFile(outputPath, sb.toString());
    }
}
