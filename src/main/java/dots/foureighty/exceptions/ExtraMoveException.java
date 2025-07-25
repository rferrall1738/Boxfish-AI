package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;
import dots.foureighty.players.LinePlayer;

public class ExtraMoveException extends BadLineException {
    public ExtraMoveException(LinePlayer player, Line line) {
        super("An extra move was played when a box was not completed", player, line);
    }
}
