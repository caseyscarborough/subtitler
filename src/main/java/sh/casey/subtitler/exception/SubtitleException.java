package sh.casey.subtitler.exception;

public class SubtitleException extends RuntimeException {

    public SubtitleException(final String message) {
        super(message);
    }

    public SubtitleException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
