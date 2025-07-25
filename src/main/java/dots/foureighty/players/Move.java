package dots.foureighty.players;

import dots.foureighty.exceptions.InvalidMoveException;
import dots.foureighty.gamebuilder.Board;
import dots.foureighty.lines.Line;

import java.util.ArrayList;

public class Move {
    private final ArrayList<Line>  lines;
    public Move(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
    public void validate(Board board) throws InvalidMoveException {
        //TODO: add logic
    }
}
