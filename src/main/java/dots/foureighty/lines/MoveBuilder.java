package dots.foureighty.lines;

import dots.foureighty.exceptions.EmptyMoveException;
import dots.foureighty.exceptions.MissingExtraMoveException;
import dots.foureighty.game.boards.Board;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MoveBuilder {
    private final Board startingBoard;
    private final LinkedList<Line> moves = new LinkedList<>();
    private final LinkedList<Point[]> createdBoxes = new LinkedList<>();
    private Board currentBoard;

    public MoveBuilder(Board board) {
        this.startingBoard = board;
        currentBoard = board;
    }

    public Move build() {
        if (moves.isEmpty()) {
            throw new EmptyMoveException();
        }
        if (!isComplete()) {
            throw new MissingExtraMoveException();
        }
        return new Move(new ArrayList<>(moves));
    }

    public void addLine(Line line) {
        moves.add(line);
        createdBoxes.add(currentBoard.getCompletedBoxLocations(line).toArray(new Point[0]));
        currentBoard = currentBoard.append(line);
    }

    public boolean isComplete() {
        return (moves.size() > 0 && createdBoxes.getLast().length == 0) ||
                currentBoard.getUnplayedPositions().isEmpty();
    }

    public Point[] getNewBoxes() {
        final LinkedList<Point> newBoxes = new LinkedList<>();
        for (Point[] boxArray : createdBoxes) {
            Collections.addAll(newBoxes, boxArray);
        }
        return newBoxes.toArray(new Point[0]);
    }

    public Line[] getLines() {
        return moves.toArray(new Line[0]);
    }

    public boolean isEmpty() {
        return moves.isEmpty();
    }

    public boolean canPlay(Line line) {
        return !currentBoard.containsLine(line);
    }

    public void removeLastLine() {
        moves.removeLast();
        createdBoxes.removeLast();
        Board newBoard = startingBoard;
    }


    public void clear() {
        currentBoard = startingBoard;
        moves.clear();
        createdBoxes.clear();
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }
}
