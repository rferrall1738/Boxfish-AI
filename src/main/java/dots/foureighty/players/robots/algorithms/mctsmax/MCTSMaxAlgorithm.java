package dots.foureighty.players.robots.algorithms.mctsmax;

import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.players.robots.algorithms.mcts.MCTSSearchAlgorithm;
import dots.foureighty.players.robots.algorithms.minimax.AlphaBetaSearchAlgorithm;
import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class MCTSMaxAlgorithm<InputType, TransitionType> extends AlphaBetaSearchAlgorithm<InputType, TransitionType> {

    private final MCTSSearchAlgorithm<InputType, TransitionType> mcts;
    private final int defaultDepth;
    private final double switchRate;
    private Integer initialBranchingFactor = null;
    private boolean usingAlphaBeta = false;


    /**
     * Initializes the MCTS and Alpha-Beta hybrid algorithm with provided parameters.
     *
     * @param mcts instance of MCTS
     * @param abDepth default depth for Alpha-Beta search
     * @param switchRate A value between 0.0 and 1.0 that specifies when to switch algorithms
     *                   between MCTS and Alpha-Beta search during execution.
     * @throws NullPointerException If the specified MCTS instance is null.
     * @throws IllegalArgumentException If the provided switchRate is not within the range [0.0, 1.0].
     */
    public MCTSMaxAlgorithm(MCTSSearchAlgorithm<InputType, TransitionType> mcts, int abDepth, double switchRate) {
        this.mcts = Objects.requireNonNull(mcts, "mcts must not be null");

        if (switchRate <0.0|| switchRate >1.0) {
            throw new IllegalArgumentException("Switch rate must be between 0.0 and 1.0");
        }
        this.defaultDepth = abDepth;
        this.switchRate = switchRate;
    }


    /**
     * Provides best path of game state by using a combination of MCTS and Alpha-Beta search.
     *
     * @param input The initial state on which the search is performed.
     * @param neighborGenerator The generator to create neighbors and transitional states.
     * @param evaluator The evaluator to find the best move
     * @param depth Limits the depth of the search tree
     * @param maximize boolean if maximizing or minimizing
     * @return A pair where the key contains a linked list of transitions forming the best path
     */
    @Override
    protected Pair<LinkedList<TransitionType>, Float> search(InputType input,
                                                             NeighborGenerator<InputType, TransitionType> neighborGenerator,
                                                             Evaluator<InputType> evaluator,
                                                             int depth,
                                                             boolean maximize) throws InterruptedException {
        final int abDepthOnce = depth < 0 ? defaultDepth : depth;

        if (usingAlphaBeta) {
                return super.search(input, neighborGenerator, evaluator, abDepthOnce, maximize);
        }

        final int neighborCount = countNeighbors(input, neighborGenerator);
        if (initialBranchingFactor == null) {
            initialBranchingFactor = Math.max(neighborCount, 1);
        }

        final double neighborRatio = (double) neighborCount / (double) initialBranchingFactor;
        final boolean switchToAB = neighborRatio <= switchRate;

        if (switchToAB) {
            usingAlphaBeta = true;
            try {
                return super.search(input, neighborGenerator, evaluator, abDepthOnce, maximize);
            } catch (InterruptedException e) {
                //TODO: Fix this
                throw new RuntimeException(e);
            }
        }

        return mcts.search(input, neighborGenerator, evaluator);
    }

    public void reset(){
        this.initialBranchingFactor = null;
        this.usingAlphaBeta = false;
    }

    private int countNeighbors(InputType state, NeighborGenerator<InputType, TransitionType> neighborGenerator) {
        int n = 0;
        final Iterator<Pair<InputType, TransitionType>> neighbors = neighborGenerator.getNeighbors(state);
        while (neighbors.hasNext()) {
            neighbors.next();
            n++;
        }
        return n;
    }
}