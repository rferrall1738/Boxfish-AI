package dots.foureighty.lines;

import java.util.ArrayList;

public class PlayedMove extends Move {
    private final boolean player1;

    public PlayedMove(ArrayList<Line> lines, boolean player1) {
        super(lines);
        this.player1 = player1;
    }

    public boolean isFromPlayerOne() {
        return player1;
    }
}
