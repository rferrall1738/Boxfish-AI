package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;

public abstract class BadLineException extends InvalidMoveException {
  private final Line line;
    public BadLineException(String message, Line line) {
        super(message);
        this.line = line;
    }
}
