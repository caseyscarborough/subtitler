package sh.casey.subtitler.shifter;

public class ShiftConfig {

    private final String after;
    private final String before;
    private final String matches;
    private final Integer number;
    private final Long ms;
    private final ShiftMode shiftMode;

    ShiftConfig(final String after, final String before, final String matches, final Integer number, final Long ms, final ShiftMode shiftMode) {
        this.after = after;
        this.before = before;
        this.matches = matches;
        this.number = number;
        this.ms = ms;
        this.shiftMode = shiftMode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getAfter() {
        return after;
    }

    public String getBefore() {
        return before;
    }

    public String getMatches() {
        return matches;
    }

    public Integer getNumber() {
        return number;
    }

    public Long getMs() {
        return ms;
    }

    public ShiftMode getShiftMode() {
        return shiftMode;
    }

    public static class Builder {
        private String after;
        private String before;
        private String matches;
        private Integer number;
        private Long ms;
        private ShiftMode shiftMode = ShiftMode.FROM_TO;

        Builder() {
        }

        public Builder after(final String after) {
            this.after = after;
            return this;
        }

        public Builder before(final String before) {
            this.before = before;
            return this;
        }

        public Builder matches(final String matches) {
            this.matches = matches;
            return this;
        }

        public Builder number(final Integer number) {
            this.number = number;
            return this;
        }

        public Builder ms(final Long ms) {
            this.ms = ms;
            return this;
        }

        public Builder shiftMode(final ShiftMode shiftMode) {
            this.shiftMode = shiftMode;
            return this;
        }

        public ShiftConfig build() {
            return new ShiftConfig(after, before, matches, number, ms, shiftMode);
        }
    }
}
