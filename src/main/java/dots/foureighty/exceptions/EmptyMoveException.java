package dots.foureighty.exceptions;

public class EmptyMoveException extends InvalidMoveException {
    public EmptyMoveException() {
        super("A move has been played with no lines");
    }
}
