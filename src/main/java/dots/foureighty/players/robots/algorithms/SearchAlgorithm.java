package dots.foureighty.players.robots.algorithms;

import dots.foureighty.util.Pair;

import java.util.LinkedList;

public abstract class SearchAlgorithm<NodeType, TransitionType> {


    /***
     * Searches the tree for the best output, and it's evaluation
     * @param input The starting node.
     * @param neighborGenerator Function to generate neighbors from a node.
     * @param evaluator Function to evaluate leaves
     * @return
     */
    protected abstract Pair<LinkedList<TransitionType>, Float> search(NodeType input,
                                                                      NeighborGenerator<NodeType, TransitionType> neighborGenerator,
                                                                      Evaluator<NodeType> evaluator);
}
