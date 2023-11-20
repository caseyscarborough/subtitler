package sh.casey.subtitler.model;

public enum SubtitleType {

    ASS(".ass", "H:mm:ss.SS"),
    SRT(".srt", "HH:mm:ss,SSS"),
    TTML(".ttml", "HH:mm:ss.SSS"),
    DFXP(".dfxp", "HH:mm:ss.SSS"),
    VTT(".vtt", "HH:mm:ss.SSS"),
    SSA(".ssa", ASS.getTimeFormat()),
    LRC(".lrc", "mm:ss.SS");

    private final String extension;
    private final String timeFormat;

    SubtitleType(final String extension, final String timeFormat) {
        this.extension = extension;
        this.timeFormat = timeFormat;
    }

    public static SubtitleType find(final String s) {
        for (final SubtitleType value : values()) {
            if (value.name().equalsIgnoreCase(s) || value.extension.equalsIgnoreCase(s)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Could not find subtitle type for '" + s + "'");
    }

    public String getExtension() {
        return extension;
    }

    public String getTimeFormat() {
        return timeFormat;
    }
}
