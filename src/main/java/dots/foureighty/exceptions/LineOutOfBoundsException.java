package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;
import dots.foureighty.players.LinePlayer;

public class LineOutOfBoundsException extends BadLineException {
    public LineOutOfBoundsException(LinePlayer player, Line line) {
        super("Played line is out of bounds of the board", player, line);
    }
}
