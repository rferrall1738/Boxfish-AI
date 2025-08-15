package dots.foureighty.players.robots.searchbots.minimax;

import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveIterator;
import dots.foureighty.players.robots.SkippableNeighborGenerator;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

import java.util.NoSuchElementException;

public class MinimaxNeighborGenerator extends SkippableNeighborGenerator<DABState, Move> {

    @Override
    public SkippableIterator<Pair<DABState, Move>> getNeighbors(DABState input) {
        final MoveIterator moveIterator = new MoveIterator(input.getBoard());
        return new SkippableIterator<Pair<DABState, Move>>() {

            @Override
            public void pruneCurrentBranch() throws NoSuchElementException {
                moveIterator.skipBranch();
            }

            @Override
            public boolean hasNext() {
                return moveIterator.hasNext();
            }

            @Override
            public Pair<DABState, Move> next() {
                Move nextMove = moveIterator.next();
                if(nextMove == null) {
                    System.out.println(moveIterator);
                }
                return new Pair<>(input.withMove(nextMove), nextMove);
            }
        };
    }
}
