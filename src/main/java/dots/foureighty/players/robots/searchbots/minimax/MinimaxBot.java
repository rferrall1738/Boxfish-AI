package dots.foureighty.players.robots.searchbots.minimax;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveIterator;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.players.robots.algorithms.minimax.MinimaxSearchAlgorithm;
import dots.foureighty.util.ColorUtils;
import dots.foureighty.util.Pair;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;


public class MinimaxBot extends MinimaxSearchAlgorithm<MinimaxState, Move> implements Player {
    protected final int depth;
    private Color color = Color.ORANGE;
    private final Heuristic<MinimaxState>[] heuristics;

    public MinimaxBot(int depth, Heuristic<MinimaxState>... heuristics) {
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
        return "MinimaxBot (" + depth + ")";
    }

    protected final Evaluator stateEvaluator = new Evaluator<MinimaxState>() {

        /***
         * Evaluator for a position
         * @param input Final state to evaluate.
         * @return an estimate of this player's score minus the opponent's score.
         */
        @Override
        public float evaluate(MinimaxState input) {
            float score = input.getSelfScore() - input.getOpponentScore();
            for (Heuristic<MinimaxState> heuristic : heuristics) {
                score += heuristic.evaluate(input);
            }
            return score;
        }
    };


    protected final NeighborGenerator neighborGenerator = new NeighborGenerator<MinimaxState, Move>() {
        @Override
        public Iterator<Pair<MinimaxState, Move>> getNeighbors(MinimaxState input) {
            final MoveIterator moveIterator = new MoveIterator(input.getBoard());
            return new Iterator<Pair<MinimaxState, Move>>() {

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
        Pair<LinkedList<Move>, Float> result = search(initialState, neighborGenerator, stateEvaluator, depth, true);

        return result.getKey().getFirst();
    }


}
