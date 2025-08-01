package dots.foureighty.lines;

import dots.foureighty.game.boards.Board;
import dots.foureighty.util.Pair;

import java.util.*;

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

}
