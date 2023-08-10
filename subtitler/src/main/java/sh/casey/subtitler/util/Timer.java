package sh.casey.subtitler.util;

import java.util.concurrent.TimeUnit;

public class Timer {

    private Long start;

    public void start() {
        if (start != null) {
            throw new IllegalStateException("Timer must be stopped to start it.");
        }
        start = System.nanoTime();
    }

    public long stop() {
        if (start == null) {
            throw new IllegalStateException("Timer must be started to stop it.");
        }
        final long elapsed = System.nanoTime() - start;
        start = null;
        return TimeUnit.NANOSECONDS.toMillis(elapsed);
    }
}
