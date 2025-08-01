package dots.foureighty.lines;

import java.util.LinkedList;

public class PlayedMove extends Move {
    private final boolean player1;

    public PlayedMove(LinkedList<Line> lines, boolean player1) {
        super(lines);
        this.player1 = player1;
    }

    public boolean isFromPlayerOne() {
        return player1;
    }
}
