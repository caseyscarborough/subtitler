package sh.casey.subtitler.util;

import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.model.SubtitleType;

public class TimeUtil {

    private static final Long MILLISECONDS = 1L;
    private static final Long SECONDS = MILLISECONDS * 1000;
    private static final Long MINUTES = SECONDS * 60;
    private static final Long HOURS = MINUTES * 60;

    public static Long formatTimeToMilliseconds(SubtitleType type, String time) {
        if (type == SubtitleType.SRT) {
            return srtFormatTimeToMilliseconds(time);
        } else if (type == SubtitleType.ASS) {
            return assFormatTimeToMilliseconds(time);
        } else if (type == SubtitleType.VTT) {
            // TODO: verify this works
            return timeToMilliseconds(".", time, 1);
        } else {
            throw new IllegalArgumentException("Could not format time for type " + type);
        }
    }

    public static String millsecondsToTime(SubtitleType type, Long time) {
        if (type == SubtitleType.SRT) {
            return srtMillisecondsToTime(time);
        } else if (type == SubtitleType.ASS) {
            return assMillisecondsToTime(time);
        } else if (type == SubtitleType.VTT) {
            return vttMillisecondsToTime(time);
        } else {
            throw new IllegalArgumentException("Could not format time for type " + type + " as the conversion has not been implemented");
        }
    }

    public static String assMillisecondsToTime(Long time) {
        long hours = time / HOURS;
        time %= HOURS;
        long minutes = time / MINUTES;
        time %= MINUTES;
        long seconds = time / SECONDS;
        time %= SECONDS;
        long ms = time;
        return hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", ms).substring(0, 2);
    }

    public static String srtMillisecondsToTime(Long time) {
        return millisecondsToTime(time, ",");
    }

    public static String vttMillisecondsToTime(Long time) {
        return millisecondsToTime(time, ".");
    }

    private static String millisecondsToTime(Long time, String separator) {
        long hours = time / HOURS;
        time %= HOURS;
        long minutes = time / MINUTES;
        time %= MINUTES;
        long seconds = time / SECONDS;
        time %= SECONDS;
        long ms = time;
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + separator + String.format("%03d", ms);
    }

    public static Long srtFormatTimeToMilliseconds(final String time) {
        return timeToMilliseconds(",", time, 1);
    }

    public static Long assFormatTimeToMilliseconds(final String time) {
        return timeToMilliseconds(".", time, 10);
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
