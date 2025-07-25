package dots.foureighty.exceptions;

import dots.foureighty.players.LinePlayer;

public class MissingExtraMoveException extends InvalidMoveException {
    public MissingExtraMoveException(String message, LinePlayer player) {
        super("Missing the extra move", player);
    }
}
