package dots.foureighty.exceptions;

public class MissingExtraMoveException extends InvalidMoveException {
    public MissingExtraMoveException() {
        super("Missing the extra move");
    }
}
