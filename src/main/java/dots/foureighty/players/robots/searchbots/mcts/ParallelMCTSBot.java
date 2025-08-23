package dots.foureighty.players.robots.searchbots.mcts;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.mcts.ParallelMCTSAlgorithm;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.util.ColorUtils;
import java.awt.*;

public class ParallelMCTSBot extends ParallelMCTSAlgorithm implements Player {
    private Color color = Color.BLACK;
    private final int iterations;
    private static final Evaluator<DABState> DEFAULT_EVAL = new Evaluator<DABState>() {
        @Override
        public float evaluate(DABState input) {
            return input.getSelfScore() - input.getOpponentScore();
        }
    };


    private static final int DEFAULT_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors());
    private static final int DEFAULT_MAX_ROLLOUT_LEN = 100;

    public ParallelMCTSBot(int maxIterations) throws IllegalArgumentException {
        this(maxIterations, Math.sqrt(2));
    }

    public ParallelMCTSBot(int maxIterations, double explorationParameter) throws IllegalArgumentException {
        super(DEFAULT_EVAL, maxIterations, explorationParameter, DEFAULT_THREADS, DEFAULT_MAX_ROLLOUT_LEN);
        this.iterations = maxIterations;
    }

    @Override
    public Color getColor() { return color; }

    @Override
    public void setColor(Color color) { this.color = ColorUtils.withFullAlpha(color); }

    @Override
    public String getName() { return "ParallelMCTS (" + iterations + ")"; }

    @Override
    public Move getMove(GameSnapshot gameSnapshot) {
        DABState initialState = new DABState(gameSnapshot.getBoard());
        Result result = search(initialState);
        return result.bestMove;
    }
}
