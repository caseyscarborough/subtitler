package sh.casey.subtitler.util;

import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.model.SubtitleType;

public class TimeUtil {

    private static final Long MILLISECONDS = 1L;
    private static final Long SECONDS = MILLISECONDS * 1000;
    private static final Long MINUTES = SECONDS * 60;
    private static final Long HOURS = MINUTES * 60;

    public static Long timeToMilliseconds(SubtitleType type, String time) {
        if (type == SubtitleType.SRT) {
            return timeToMilliseconds(",", time, 1);
        } else if (type == SubtitleType.ASS) {
            return timeToMilliseconds(".", time, 10);
        } else if (type == SubtitleType.VTT) {
            return timeToMilliseconds(".", time, 1);
        } else if (type == SubtitleType.DFXP) {
            return timeToMilliseconds(".", time, 1);
        } else {
            throw new IllegalArgumentException("Could not format time for type " + type);
        }
    }

    public static String millisecondsToTime(SubtitleType type, long time) {
        if (type == SubtitleType.SRT) {
            return millisecondsToTime(time, 2, 3, ",");
        } else if (type == SubtitleType.ASS) {
            return millisecondsToTime(time, 1, 2, ".");
        } else if (type == SubtitleType.VTT) {
            return millisecondsToTime(time, 2, 3, ".");
        } else if (type == SubtitleType.DFXP) {
            return millisecondsToTime(time, 2, 3, ".");
        } else {
            throw new IllegalArgumentException("Could not format time for type " + type + " as the conversion has not been implemented");
        }
    }

    private static String millisecondsToTime(long time, int hoursDigits, int msDigits, String separator) {
        long hours = time / HOURS;
        time %= HOURS;
        long minutes = time / MINUTES;
        time %= MINUTES;
        long seconds = time / SECONDS;
        time %= SECONDS;
        long ms = time;
        for (int i = 3 - msDigits; i > 0; i--) {
            ms /= 10;
        }
        String format = String.format("%%0%dd:%%02d:%%02d%%s%%0%dd", hoursDigits, msDigits);
        return String.format(format, hours, minutes, seconds, separator, ms);
    }

    public static Long timeToMilliseconds(final String replace, final String time, Integer msFactor) {
        final String[] parts = time.replace(replace, ":").split(":");
        long milliseconds = 0L;
        for (int i = 0; i < parts.length; i++) {
            final long amount = Long.parseLong(parts[i]);
            if (i == 0) {
                milliseconds += amount * 3600000;
            } else if (i == 1) {
                milliseconds += amount * 60000;
            } else if (i == 2) {
                milliseconds += amount * 1000;
            } else if (i == 3) {
                milliseconds += amount * msFactor;
            }
        }
        return milliseconds;
    }

    // This method removes the 3rd millisecond character
    // from .ass subtitles, e.g. 1:00:00.333 => 1:00:00.33
    public static String assTrim(final String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }

        if (time.length() > 10) {
            return time.substring(0, 10);
        }
        return time;
    }
}
