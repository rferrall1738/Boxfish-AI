package dots.foureighty.players.robots.algorithms;

import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class SearchAlgorithm<NodeType, TransitionType> {
    protected abstract class NeighborGenerator {
        /***
         * Generate neighbors from a node.
         * @param input Node to generate from.
         * @return List of neighbors, and the input required to transition to the state.
         */
        protected abstract Iterator<Pair<NodeType, TransitionType>> getNeighbors(NodeType input);
    }

    protected abstract class Evaluator {
        /***
         * When given a final state, evaluates how good the output is
         * @param input Final state to evaluate.
         * @return Metric to determine how good the output is.
         */
        protected abstract float evaluate(NodeType input);
    }

    /***
     * Searches the tree for the best output, and it's evaluation
     * @param input The starting node.
     * @param neighborGenerator Function to generate neighbors from a node.
     * @param evaluator Function to evaluate leaves
     * @return
     */
    protected abstract Pair<LinkedList<TransitionType>, Float> search(NodeType input,
                                                                      NeighborGenerator neighborGenerator,
                                                                      Evaluator evaluator);
}
