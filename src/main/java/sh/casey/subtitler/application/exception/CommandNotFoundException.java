package sh.casey.subtitler.application.exception;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException(final String message) {
        super(message);
    }
}
