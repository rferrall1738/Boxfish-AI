package dots.foureighty.moves;

import dots.foureighty.Player;
import dots.foureighty.lines.PlayedLine;

public class Move {
    private final PlayedLine[] playedLines;
    private final Player player;
    public Move(PlayedLine[] playedLines, Player player) {
        this.playedLines = playedLines;
        this.player = player;
    }
    public PlayedLine[] getLines() {
        return playedLines;
    }
    public Player getPlayer() {
        return player;
    }

}
