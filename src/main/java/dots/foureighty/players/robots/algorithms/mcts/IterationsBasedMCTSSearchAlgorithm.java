package dots.foureighty.players.robots.algorithms.mcts;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.players.robots.algorithms.mcts.conditional.IterationsBasedConditional;
import dots.foureighty.util.Pair;

import java.util.*;

public class IterationsBasedMCTSSearchAlgorithm<NodeType, TransitionType> extends MCTSSearchAlgorithmBase<NodeType, TransitionType> {

    private final int maxIterations;
    private int numberOfRuns = 0;

    /***
     * Monte Carlo Tree Search
     * @param maxIterations number of iterations to run MCTS
     * @param explorationParameter Exploration coefficient. Must be >= 0.
     *                             Higher values prioritize exploration of new nodes.
     * @throws IllegalArgumentException explorationParameter is < 0
     */
    public IterationsBasedMCTSSearchAlgorithm(int maxIterations, double explorationParameter) throws IllegalArgumentException{
        super(explorationParameter,new IterationsBasedConditional(maxIterations));
        this.maxIterations = maxIterations;
        if (explorationParameter < 0) {
            throw new IllegalArgumentException("explorationParameter must be >= 0");
        }
    }

    /***
     * Monte Carlo Tree Search
     * exploration constant in sqrt(2)
     * @param maxIterations number of MCTS iterations
     */
    public IterationsBasedMCTSSearchAlgorithm(int maxIterations) {
        this(maxIterations, Math.sqrt(2.0));
    }

    @Override
    public Pair<LinkedList<TransitionType>, Float> search(NodeType input, NeighborGenerator<NodeType, TransitionType> neighborGenerator, Evaluator<NodeType> evaluator) {
        numberOfRuns = 0;
        return super.search(input, neighborGenerator, evaluator);
    }
}
