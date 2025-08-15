package dots.foureighty.players.robots.searchbots.minimax;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.minimax.AlphaBetaSearchAlgorithm;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.util.ColorUtils;
import dots.foureighty.util.Pair;

import java.awt.*;
import java.util.LinkedList;


public class AlphaBetaBot extends AlphaBetaSearchAlgorithm<DABState, Move> implements Player {
    protected final int depth;
    private Color color = Color.ORANGE;
    private final Heuristic<DABState>[] heuristics;


    public AlphaBetaBot(int depth, Heuristic<DABState>... heuristics) {
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

    protected final Evaluator stateEvaluator = new Evaluator<DABState>() {
        @Override
        public float evaluate(DABState input) {
            float score = input.getSelfScore() - input.getOpponentScore();
            for (Heuristic<DABState> heuristic : heuristics) {
                score += 50 * heuristic.evaluate(input);
            }
            return score;
        }
    };



    @Override
    public Move getMove(GameSnapshot gameState) {
        DABState initialState = new DABState(gameState.getBoard());
        Pair<LinkedList<Move>, Float> result = search(initialState, new MinimaxNeighborGenerator(), stateEvaluator, depth, true, -Float.MAX_VALUE, Float.MAX_VALUE);
        return result.getKey().getFirst();
    }
}
