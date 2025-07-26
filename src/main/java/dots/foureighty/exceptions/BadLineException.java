package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;

public abstract class BadLineException extends InvalidMoveException {
    public BadLineException(String message, Line line) {
        super(message + " " + line);
    }
}
