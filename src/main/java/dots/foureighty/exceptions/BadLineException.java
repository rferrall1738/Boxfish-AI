package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;
import dots.foureighty.players.LinePlayer;

public abstract class BadLineException extends InvalidMoveException {
  private final Line line;
    public BadLineException(String message, LinePlayer player, Line line) {
        super(message, player);
        this.line = line;
    }
}
