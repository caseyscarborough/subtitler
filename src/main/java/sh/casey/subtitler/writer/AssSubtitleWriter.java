package sh.casey.subtitler.writer;

import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
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

        if (StringUtils.isNotBlank(file.getScriptInfo().getTitle())) {
            sb.append("Title: ").append(file.getScriptInfo().getTitle()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getScriptType())) {
            sb.append("ScriptType: ").append(file.getScriptInfo().getScriptType()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getCollisions())) {
            sb.append("Collisions: ").append(file.getScriptInfo().getCollisions()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getWrapStyle())) {
            sb.append("WrapStyle: ").append(file.getScriptInfo().getWrapStyle()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getPlayResX())) {
            sb.append("PlayResX: ").append(file.getScriptInfo().getPlayResX()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getPlayResY())) {
            sb.append("PlayResY: ").append(file.getScriptInfo().getPlayResY()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getScaledBorderAndShadow())) {
            sb.append("ScaledBorderAndShadow: ").append(file.getScriptInfo().getScaledBorderAndShadow()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getVideoAspectRatio())) {
            sb.append("Video Aspect Ratio: ").append(file.getScriptInfo().getVideoAspectRatio()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getVideoZoom())) {
            sb.append("Video Zoom: ").append(file.getScriptInfo().getVideoZoom()).append(CRLF);
        }

        if (StringUtils.isNotBlank(file.getScriptInfo().getVideoPosition())) {
            sb.append("Video Position: ").append(file.getScriptInfo().getVideoPosition()).append(CRLF);
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
