package sh.casey.subtitler.writer;

import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

public class AssSubtitleWriter implements SubtitleWriter<AssSubtitleFile> {

    private static final String CRLF = "\r\n";

    @Override
    public void write(final AssSubtitleFile file, final String outputPath) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Script Info]").append(CRLF);

        for (final String comment : file.getComments()) {
            sb.append(comment).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getTitle())) {
            sb.append("Title: ").append(file.getTitle()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptType())) {
            sb.append("ScriptType: ").append(file.getScriptType()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getCollisions())) {
            sb.append("Collisions: ").append(file.getCollisions()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getWrapStyle())) {
            sb.append("WrapStyle: ").append(file.getWrapStyle()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getPlayResX())) {
            sb.append("PlayResX: ").append(file.getPlayResX()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getPlayResY())) {
            sb.append("PlayResY: ").append(file.getPlayResY()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScaledBorderAndShadow())) {
            sb.append("ScaledBorderAndShadow: ").append(file.getScaledBorderAndShadow()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getVideoAspectRatio())) {
            sb.append("Video Aspect Ratio: ").append(file.getVideoAspectRatio()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getVideoZoom())) {
            sb.append("Video Zoom: ").append(file.getVideoZoom()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getVideoPosition())) {
            sb.append("Video Position: ").append(file.getVideoPosition()).append(CRLF);
        }

        if (!file.getStyles().isEmpty()) {
            sb.append(CRLF)
                .append("[V4+ Styles]")
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
                    final String value = dialogue.getValue(key);
                    sb.append(value != null ? value : "");
                    first = false;
                }
                sb.append(CRLF);
            }
        }

        FileUtils.writeFile(outputPath, sb.toString());
    }
}
