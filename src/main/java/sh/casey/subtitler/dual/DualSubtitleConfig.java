package sh.casey.subtitler.dual;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sh.casey.subtitler.application.command.config.StyleConfig;

import java.util.EnumMap;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DualSubtitleConfig {

    // Keep the styles from the origin top file
    private final boolean keepTopStyles;

    // Move all subtitles on the top of the bottom file to the bottom
    private final boolean align;

    // The style config for the top file
    private final Map<StyleConfig, String> topStyleConfig;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<StyleConfig, String> topStyleConfig = new EnumMap<>(StyleConfig.class);
        private boolean keepTopStyles = false;
        private boolean align = false;

        public Builder keepTopStyles(boolean keepTopStyles) {
            this.keepTopStyles = keepTopStyles;
            return this;
        }

        public Builder align(boolean align) {
            this.align = align;
            return this;
        }

        public Builder topStyles(Map<StyleConfig, String> topStyleConfig) {
            this.topStyleConfig.putAll(topStyleConfig);
            return this;
        }

        public Builder topStyle(StyleConfig config, String value) {
            topStyleConfig.put(config, value);
            return this;
        }

        public DualSubtitleConfig build() {
            return new DualSubtitleConfig(keepTopStyles, align, topStyleConfig);
        }
    }
}
