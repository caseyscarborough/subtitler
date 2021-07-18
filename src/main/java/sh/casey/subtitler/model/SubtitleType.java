package sh.casey.subtitler.model;

public enum SubtitleType {

    ASS(".ass", "H:mm:ss.SS"),
    SRT(".srt", "HH:mm:ss,SSS"),
    TTML(".ttml", "HH:mm:ss.SSS"),
    DFXP(".dfxp", "HH:mm:ss.SSS");

    private final String extension;
    private final String timeFormat;

    SubtitleType(String extension, String timeFormat) {
        this.extension = extension;
        this.timeFormat = timeFormat;
    }

    public static SubtitleType find(String s) {
        for (SubtitleType value : values()) {
            if (value.name().equalsIgnoreCase(s)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Could not find subtitle by string '" + s + "'");
    }

    public String getExtension() {
        return extension;
    }

    public String getTimeFormat() {
        return timeFormat;
    }
}
