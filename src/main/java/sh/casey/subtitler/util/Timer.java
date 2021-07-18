package sh.casey.subtitler.util;

public class Timer {

    private Long start;

    public void start() {
        if (start != null) {
            throw new IllegalStateException("Timer must be stopped to start it.");
        }
        start = System.currentTimeMillis();
    }

    public long stop() {
        if (start == null) {
            throw new IllegalStateException("Timer must be started to stop it.");
        }
        long elapsed = System.currentTimeMillis() - start;
        start = null;
        return elapsed;
    }
}
