package dots.foureighty.lines;

import dots.foureighty.game.boards.Board;
import dots.foureighty.util.Pair;

import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class MoveIterator implements Iterator<Move> {
    private final Board board;
    private int index = -1;
    private final BitSet unplayedBitSet;
    private final LinkedList<Pair<Line, MoveIterator>> queuedIterators = new LinkedList<>();

    public MoveIterator(Board board) {
        this.board = board;
        unplayedBitSet = board.getInverseBitSet();
    }

    @Override
    public boolean hasNext() {
        if (!queuedIterators.isEmpty()) {
            return queuedIterators.getFirst().getValue().hasNext();
        }
        return unplayedBitSet.nextSetBit(index + 1) != -1;
    }

    //TODO: Check for duplicate moves. Move iterator should not return equal moves. (Line A, Line B, Line C)
    // is the same as (Line B, Line A, Line C)
    @Override
    public Move next() {
        if (queuedIterators.isEmpty()) {
            index = unplayedBitSet.nextSetBit(index + 1);
            if (index == -1) {
                return null;
            }
            Line line = board.getLineFromIndex(index);
            if (board.completesBox(index) && unplayedBitSet.cardinality() > 1) {
                queuedIterators.add(new Pair<>(line, new MoveIterator(board.append(line))));
                return next();
            }

            return new Move(new LinkedList<>(Collections.singletonList(line)));

        } else {
            Pair<Line, MoveIterator> childIteratorPair = queuedIterators.peekFirst();
            MoveIterator childIterator = childIteratorPair.getValue();
            Move move = childIterator.next();
            Line line = board.getLineFromIndex(index);
            move.getLines().addFirst(line);

            if (!childIterator.hasNext()) {
                queuedIterators.removeFirst();
            }
            return move;
        }
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
}
