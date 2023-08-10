package sh.casey.subtitler.shifter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public enum ShiftMode {
    FROM("from", "shifts only the start time in each subtitle"),
    FROM_TO("from-to", "shifts the start and end times in the subtitle (default)"),
    TO("to", "shifts only the end time in each subtitle");

    private final String flag;
    private final String explanation;

    public static ShiftMode findByString(final String s) {
        if (StringUtils.isBlank(s)) {
            return FROM_TO;
        }

        for (final ShiftMode value : values()) {
            if (s.toUpperCase().matches(value.name())) {
                return value;
            }

            if (s.equalsIgnoreCase(value.flag)) {
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
