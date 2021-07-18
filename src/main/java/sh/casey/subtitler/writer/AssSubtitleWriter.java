package sh.casey.subtitler.writer;

import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;
import sh.casey.subtitler.util.FileUtils;

public class AssSubtitleWriter implements SubtitleWriter<AssSubtitleFile> {

    @Override
    public void write(AssSubtitleFile file, String outputPath) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Script Info]").append("\r\n");

        for (String comment : file.getComments()) {
            sb.append(comment).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getTitle())) {
            sb.append("Title: ").append(file.getTitle()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getScriptType())) {
            sb.append("ScriptType: ").append(file.getScriptType()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getCollisions())) {
            sb.append("Collisions: ").append(file.getCollisions()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getWrapStyle())) {
            sb.append("WrapStyle: ").append(file.getWrapStyle()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getPlayResX())) {
            sb.append("PlayResX: ").append(file.getPlayResX()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getPlayResY())) {
            sb.append("PlayResY: ").append(file.getPlayResY()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getScaledBorderAndShadow())) {
            sb.append("ScaledBorderAndShadow: ").append(file.getScaledBorderAndShadow()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getVideoAspectRatio())) {
            sb.append("Video Aspect Ratio: ").append(file.getVideoAspectRatio()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getVideoZoom())) {
            sb.append("Video Zoom: ").append(file.getVideoZoom()).append("\r\n");
        }

        if (StringUtils.isNotBlank(file.getVideoPosition())) {
            sb.append("Video Position: ").append(file.getVideoPosition()).append("\r\n");
        }

        if (!file.getStyles().isEmpty()) {
            sb.append("\r\n")
                .append("[V4+ Styles]")
                .append("\r\n")
                .append("Format: ")
                .append(String.join(", ", file.getStylesFormatOrder()))
                .append("\r\n");

            for (AssStyle style : file.getStyles()) {
                sb.append("Style: ");

                boolean first = true;
                for (String key : file.getStylesFormatOrder()) {
                    if (!first) {
                        sb.append(",");
                    }
                    String value = style.getValue(key);
                    sb.append(value != null ? value : "");
                    first = false;
                }
                sb.append("\r\n");
            }
        }

        if (!file.getDialogues().isEmpty()) {
            sb.append("\r\n")
                .append("[Events]")
                .append("\r\n")
                .append("Format: ")
                .append(String.join(", ", file.getEventsFormatOrder()))
                .append("\r\n");

            for (AssDialogue dialogue : file.getDialogues()) {
                sb.append(dialogue.isComment() ? "Comment: " : "Dialogue: ");
                boolean first = true;
                for (String key : file.getEventsFormatOrder()) {
                    if (!first) {
                        sb.append(",");
                    }
                    String value = dialogue.getValue(key);
                    sb.append(value != null ? value : "");
                    first = false;
                }
                sb.append("\r\n");
            }
        }

        FileUtils.writeFile(outputPath, sb.toString());
    }
}
