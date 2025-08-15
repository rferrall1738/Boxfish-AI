package dots.foureighty.players.robots.searchbots.mcts;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.mcts.MCTSSearchAlgorithm;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.searchbots.minimax.MinimaxNeighborGenerator;
import dots.foureighty.util.ColorUtils;
import dots.foureighty.util.Pair;

import java.awt.*;
import java.util.LinkedList;

public class MCTSBot  extends MCTSSearchAlgorithm<DABState,Move> implements Player {
    private Color color = Color.PINK;
    private final int iterations;

    public MCTSBot(int maxIterations) throws IllegalArgumentException {
        this(maxIterations, Math.sqrt(2)); //TODO: Test this and pick a better value
    }

    public MCTSBot(int maxIterations, double explorationParameter) throws IllegalArgumentException {
        super(maxIterations, explorationParameter);
        iterations = maxIterations;
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
        return "MonteCarlo Bot (" + iterations + ")";
    }

    protected final Evaluator stateEvaluator = new Evaluator<DABState>() {

        /***
         * Evaluator for a position
         * @param input Final state to evaluate.
         * @return an estimate of this player's score minus the opponent's score.
         */
        @Override
        public float evaluate(DABState input) {
            float score = input.getSelfScore() - input.getOpponentScore();
            return score;
        }
    };


    @Override
    public Move getMove(GameSnapshot gameState) {
        DABState initialState = new DABState(gameState.getBoard());
        Pair<LinkedList<Move>, Float> result = search(initialState, new MinimaxNeighborGenerator(), stateEvaluator);
        return result.getKey().getFirst();
    }
}
