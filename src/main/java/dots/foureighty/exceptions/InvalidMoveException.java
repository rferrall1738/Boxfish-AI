package dots.foureighty.exceptions;

import dots.foureighty.players.LinePlayer;

public class InvalidMoveException extends RuntimeException {
    private final LinePlayer player;
    public InvalidMoveException(String message, LinePlayer player) {
        super(message);
        this.player = player;
    }
    public LinePlayer getPlayer() {
        return player;
    }
}
