package dots.foureighty.players;

import dots.foureighty.exceptions.*;
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
    public Board validate(Board board) throws InvalidMoveException {
        if (lines.isEmpty()) {
            throw new EmptyMoveException();
        }
        Board newBoard = board;
        boolean hasMove = true;

        for (Line line : lines) {
            if(!hasMove) {
                throw new ExtraMoveException(line);
            }
            if (newBoard.getIndexOfLine(line) == -1) {
                throw new LineOutOfBoundsException(line);
            }
            if (newBoard.containsLine(line)) {
                throw new LineAlreadyExistsException(line);
            }
            hasMove = !newBoard.getCompletedBoxes(line).isEmpty();
            newBoard = newBoard.addLine(line);
        }
        if(hasMove) {
            throw new MissingExtraMoveException();
        }
        return newBoard;
    }
}
