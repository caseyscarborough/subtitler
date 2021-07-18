package sh.casey.subtitler.util;

import org.apache.commons.lang3.StringUtils;

public class TimeUtil {

    public static Long assFormatTimeToMilliseconds(final String time) {
        final String[] parts = time.replaceAll("\\.", ":").split(":");
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
                milliseconds += amount * 10;
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
