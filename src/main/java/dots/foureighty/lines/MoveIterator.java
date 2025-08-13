package dots.foureighty.lines;

import dots.foureighty.game.boards.Board;
import dots.foureighty.util.Pair;

import java.util.*;

public class MoveIterator implements Iterator<Move> {
    private final Board board;
    private int index = -1;
    private final BitSet unplayedBitSet;
    private final LinkedList<Pair<Line, MoveIterator>> queuedIterators = new LinkedList<>();
    private final HashSet<Board> seenBoards;
    private Move queuedNext = null;

    public MoveIterator(Board board) {
        this.board = board;
        unplayedBitSet = board.getInverseBitSet();
        seenBoards = new HashSet<>();
    }

    private MoveIterator(Board board, HashSet<Board> seenBoards) {
        this.board = board;
        unplayedBitSet = board.getInverseBitSet();
        this.seenBoards = seenBoards;
    }

    @Override
    public boolean hasNext() { //TODO: Fix this
        if(queuedNext != null){
            return true;
        }
        queuedNext = next();
        return queuedNext != null;
    }

    @Override
    public Move next() {
        if(queuedNext != null){
            Move move = queuedNext;
            queuedNext = null;
            return move;
        }

        if (queuedIterators.isEmpty()) {
            return getMove();
        } else {
            return getChildMove();
        }
    }

    private Move getMove() {
        index = unplayedBitSet.nextSetBit(index + 1);
        if (index == -1) {
            return null;
        }
        if (board.completesBox(index) && unplayedBitSet.cardinality() > 1) {
            Board newBoard = board.append(index);
            if (seenBoards.contains(newBoard)) {
                return next();
            }
            seenBoards.add(newBoard);
            queuedIterators.add(new Pair<>(board.getLineFromIndex(index), new MoveIterator(newBoard, seenBoards)));
            return next();
        }

        return new Move(new LinkedList<>(Collections.singletonList(board.getLineFromIndex(index))));
    }

    private Move getChildMove() {
        MoveIterator childIterator;
        Move move;
        do {
            childIterator = queuedIterators.peekFirst().getValue();
            if (!childIterator.hasNext()) {
                queuedIterators.removeFirst();
            }
            if(queuedIterators.isEmpty()) {
                return getMove();
            }
            move = childIterator.next();
        } while (move == null);

        if (move == null && queuedIterators.isEmpty()) {
            return getMove();
        }

        Line line = board.getLineFromIndex(index);
        move.getLines().addFirst(line);

        return move;
    }

    public boolean hasChildBranch() {
        return !queuedIterators.isEmpty();
    }

    /***
     * Skip moves in the deepest branch
     */
    public void skipBranch() {
        if (queuedIterators.isEmpty()) {
            return;
        }
        MoveIterator childIterator = queuedIterators.peekFirst().getValue();
        if (childIterator.hasChildBranch()) {
            childIterator.skipBranch();
            return;
        }
        queuedIterators.removeFirst();
    }

    @Override
    public String toString() {
        return "Index: " + index + ", Board: " + board + "Unplayed: " + unplayedBitSet  + " Queued: [" + queuedIterators + "]";
    }
}
