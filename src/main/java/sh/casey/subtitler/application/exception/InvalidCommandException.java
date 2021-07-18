package sh.casey.subtitler.application.exception;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(final String message) {
        super(message);
    }
}
