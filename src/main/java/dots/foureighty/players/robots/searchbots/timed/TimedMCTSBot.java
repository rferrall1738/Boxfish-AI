package dots.foureighty.players.robots.searchbots.timed;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.packages.ExpirableMovePackage;
import dots.foureighty.lines.packages.MovePackage;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.players.robots.algorithms.mcts.MCTSSearchAlgorithmBase;
import dots.foureighty.players.robots.algorithms.mcts.conditional.TimedBasedConditional;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.searchbots.minimax.MinimaxNeighborGenerator;
import dots.foureighty.util.ColorUtils;
import dots.foureighty.util.Pair;

import java.awt.*;
import java.util.LinkedList;

public class TimedMCTSBot extends MCTSSearchAlgorithmBase<DABState,Move> implements TimedBot {
    private Color color = Color.PINK;
    private final long thinkingTime;

    public TimedMCTSBot(long thinkingTime) throws IllegalArgumentException {
        this(thinkingTime, Math.sqrt(2));
    }

    public TimedMCTSBot(long thinkingTime, double explorationParameter) throws IllegalArgumentException {
        super(explorationParameter, new TimedBasedConditional(thinkingTime));
        this.thinkingTime = thinkingTime;
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
        return "Thinking MonteCarlo Bot (" + thinkingTime +" ms)";
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


    @Override
    public long getThinkingTime() {
        return thinkingTime;
    }
}
