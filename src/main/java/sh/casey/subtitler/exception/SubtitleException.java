package sh.casey.subtitler.exception;

public class SubtitleException extends RuntimeException {

    public SubtitleException(String message) {
        super(message);
    }

    public SubtitleException(String message, Throwable cause) {
        super(message, cause);
    }
}
