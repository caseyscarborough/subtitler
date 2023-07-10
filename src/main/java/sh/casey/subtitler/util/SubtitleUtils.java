package sh.casey.subtitler.util;

import sh.casey.subtitler.model.Subtitle;

public class SubtitleUtils {

    public static String convertSrtTimeToAssTime(final String time) {
        return time.replace(",", ".").substring(1, time.length() - 1);
    }

    public static String convertAssTimeToSrtTime(final String time) {
        return "0" + time.replace(".", ",") + "0";
    }

    public static boolean overlaps(Subtitle a, Subtitle b) {
        // Duration of this subtitle overlaps the start time of the other subtitle
        if (a.getStartMilliseconds() <= b.getStartMilliseconds() && a.getEndMilliseconds() >= b.getStartMilliseconds()) {
            return true;
        }

        // Duration of this subtitle overlaps the end time of the other subtitle
        if (a.getEndMilliseconds() >= b.getEndMilliseconds() && a.getStartMilliseconds() <= b.getEndMilliseconds()) {
            return true;
        }

        // Duration of this subtitle is within the other subtitle
        if (a.getStartMilliseconds() >= b.getStartMilliseconds() && a.getEndMilliseconds() <= b.getEndMilliseconds()) {
            return true;
        }

        // Duration of this subtitle completely overlaps the other subtitle on both sides
        return a.getStartMilliseconds() < b.getStartMilliseconds() && a.getEndMilliseconds() > b.getEndMilliseconds();
    }
}
