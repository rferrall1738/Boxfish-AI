package dots.foureighty.players.robots.searchbots.mctsmax;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.SkippableNeighborGenerator;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.mcts.MCTSSearchAlgorithm;
import dots.foureighty.players.robots.algorithms.mctsmax.MCTSMaxAlgorithm;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.searchbots.minimax.MinimaxNeighborGenerator;

import dots.foureighty.util.Pair;

import java.awt.Color;

import java.util.LinkedList;

public class MCTSMaxBot extends MCTSMaxAlgorithm<DABState, Move> implements Player {

    private final SkippableNeighborGenerator<DABState, Move> neighborGenerator;
    private final Evaluator<DABState> evaluator;
    protected final int abDepth;
    protected final double switchRate;

    private Color color = Color.BLACK;

    public MCTSMaxBot(MCTSSearchAlgorithm<DABState, Move> mcts,
                      int abDepth,
                      double switchRate,
                      Heuristic<DABState>... heuristics) {
        super(mcts, abDepth, switchRate);
        this.abDepth = abDepth;
        this.switchRate = switchRate;
        this.neighborGenerator = new MinimaxNeighborGenerator();
        this.evaluator = new Evaluator<DABState>() {
            @Override
            public float evaluate(DABState s) {
                float score = s.getSelfScore() - s.getOpponentScore();
                if (heuristics != null) {
                    for (Heuristic<DABState> h : heuristics) {
                        if (h != null) score += 50f * h.evaluate(s);
                    }
                }
                return score;
            }
        };
    }

    @Override
    public String getName() {
        return "MCTSMaxBot(depth (" + abDepth + "), switchrate (" + switchRate + "))";
    }

    @Override
    public Color getColor() { return color; }

    @Override
    public void setColor(Color color) { this.color = color; }

    @Override
    public Move getMove(GameSnapshot gameState) {
        DABState initialState = new DABState(gameState.getBoard());
        Pair<LinkedList<Move>, Float> result =
                search(initialState, neighborGenerator, evaluator, -1, /* maximize= */ true);

        LinkedList<Move> moves = result.getKey();
        return (moves != null && !moves.isEmpty()) ? moves.getFirst() : null;
    }
}