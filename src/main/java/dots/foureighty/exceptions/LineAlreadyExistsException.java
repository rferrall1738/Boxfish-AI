package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;
import dots.foureighty.players.LinePlayer;

public class LineAlreadyExistsException extends BadLineException {

    public LineAlreadyExistsException(LinePlayer player, Line line) {
        super("Move contains a line that has already been played", player,line);
    }

}
