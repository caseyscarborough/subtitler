package sh.casey.subtitler.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimerTest {
    Timer timer;

    @Before
    public void setUp() throws Exception {
        timer = new Timer();
    }

    @Test
    public void testStartStop() throws InterruptedException {
        timer.start();
        Thread.sleep(1000);
        final long elapsed = timer.stop();
        assertEquals(1000, elapsed, 100);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenAlreadyStarted() {
        timer.start();
        timer.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotStarted() {
        timer.stop();
    }
}
