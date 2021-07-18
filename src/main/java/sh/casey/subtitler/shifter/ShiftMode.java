package sh.casey.subtitler.shifter;

import org.apache.commons.lang3.StringUtils;

public enum ShiftMode {
    FROM("Shifts only the 'from' time in each subtitle"),
    FROM_TO("Shifts the 'from' and 'to' times in the subtitle (default)"),
    TO("Shifts only the 'to' time in each subtitle");

    private final String explanation;

    ShiftMode(String explanation) {
        this.explanation = explanation;
    }

    public static ShiftMode findByString(String s) {
        if (StringUtils.isBlank(s)) {
            return FROM_TO;
        }

        for (ShiftMode value : values()) {
            if (s.toUpperCase().matches(value.name())) {
                return value;
            }
        }

        return FROM_TO;
    }

    @Override
    public String toString() {
        return name() + " (" + explanation + ")";
    }
}
