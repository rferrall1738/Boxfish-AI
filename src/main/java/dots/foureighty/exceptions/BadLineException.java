package dots.foureighty.exceptions;

public abstract class BadLineException extends InvalidMoveException {
    public BadLineException(String message) {
        super(message);
    }
}
