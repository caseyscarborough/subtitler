package sh.casey.subtitler.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DualSubConfig {

    // The font for the top subtitles
    private final String font;

    // The font size for the top subtitles
    private final String size;

    // Top subtitles bold
    private final boolean bold;
}
