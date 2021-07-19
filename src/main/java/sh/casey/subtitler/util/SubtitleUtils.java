package sh.casey.subtitler.util;

public class SubtitleUtils {

    public static String convertSrtTimeToAssTime(final String time) {
        return time.replace(",", ".").substring(1, time.length() - 1);
    }

    public static String convertAssTimeToSrtTime(final String time) {
        return "0" + time.replace(".", ",") + "0";
    }
}
