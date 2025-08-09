package dots.foureighty.players.robots.searchbots.minimax;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveIterator;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.algorithms.AlphaBetaSearchAlgorithm;
import dots.foureighty.util.ColorUtils;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

import java.awt.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class AlphaBetaBot extends AlphaBetaSearchAlgorithm<MinimaxState, Move> implements Player {
    protected final int depth;
    private Color color = Color.ORANGE;
    private final Heuristic<MinimaxState>[] heuristics;


    public AlphaBetaBot(int depth, Heuristic<MinimaxState>... heuristics) {
        this.depth = depth;
        this.heuristics = heuristics;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = ColorUtils.withFullAlpha(color);
    }

    @Override
    public String getName() {
        return "AlphaBetaBot (" + depth + ")";
    }

    protected final Evaluator stateEvaluator = new Evaluator() {
        @Override
        public float evaluate(MinimaxState input) {
            float score = input.getSelfScore() - input.getOpponentScore();
            for (Heuristic<MinimaxState> heuristic : heuristics) {
                score += 50 * heuristic.evaluate(input);
            }
            return score;
        }
    };


    protected final SkippableNeighborGenerator neighborGenerator = new SkippableNeighborGenerator() {
        @Override
        protected SkippableIterator<Pair<MinimaxState, Move>> getNeighbors(MinimaxState input) {
            final MoveIterator moveIterator = new MoveIterator(input.getBoard());
            return new SkippableIterator<Pair<MinimaxState, Move>>() {

                @Override
                public void pruneCurrentBranch() throws NoSuchElementException {
                    moveIterator.skipBranch();
                }

                @Override
                public boolean hasNext() {
                    return moveIterator.hasNext();
                }

                @Override
                public Pair<MinimaxState, Move> next() {
                    Move nextMove = moveIterator.next();
                    return new Pair<>(input.withMove(nextMove), nextMove);
                }
            };
        }
    };


    @Override
    public Move getMove(GameSnapshot gameState) {
        MinimaxState initialState = new MinimaxState(gameState.getBoard());
        Pair<LinkedList<Move>, Float> result = search(initialState, neighborGenerator, stateEvaluator, depth, true, -Float.MAX_VALUE, Float.MAX_VALUE);
        return result.getKey().getFirst();
    }
}
