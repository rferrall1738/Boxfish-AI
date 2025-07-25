package dots.foureighty.exceptions;

import dots.foureighty.players.LinePlayer;

public class EmptyMoveException extends InvalidMoveException {
    public EmptyMoveException(LinePlayer player) {
        super("A move has been played with no lines", player);
    }
}
