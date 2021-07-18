package sh.casey.subtitler.model;

public enum SubtitlePosition {
    TOP("Top"),
    BOTTOM("Bot");

    private final String position;

    SubtitlePosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return getPosition();
    }
}
