package sh.casey.subtitler.dual;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DualSubtitleConfig {

    // Keep the styles from the origin top file
    private final boolean keepTopStyles;

    // Move all subtitles on the top of the bottom file to the bottom
    private final boolean align;

    // The style config for the top file
    private final Map<String, String> topStyleConfig;

    // The bottom style to apply to the top subtitle
    private final String copyStyleFrom;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, String> topStyleConfig = new HashMap<>();
        private boolean keepTopStyles = false;
        private boolean align = false;
        private String copyStyleFrom;

        public Builder keepTopStyles(boolean keepTopStyles) {
            this.keepTopStyles = keepTopStyles;
            return this;
        }

        public Builder align(boolean align) {
            this.align = align;
            return this;
        }

        public Builder topStyles(Map<String, String> topStyleConfig) {
            this.topStyleConfig.putAll(topStyleConfig);
            return this;
        }

        public Builder topStyle(String config, String value) {
            topStyleConfig.put(config, value);
            return this;
        }

        public Builder copyStyleFrom(String copyStyleFrom) {
            this.copyStyleFrom = copyStyleFrom;
            return this;
        }

        public DualSubtitleConfig build() {
            return new DualSubtitleConfig(keepTopStyles, align, topStyleConfig, copyStyleFrom);
        }
    }
}
