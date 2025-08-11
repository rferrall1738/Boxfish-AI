package dots.foureighty.players.robots.algorithms;

import dots.foureighty.util.Pair;

import java.util.Iterator;

public abstract class NeighborGenerator<NodeType, TransitionType> {
    /***
     * Generate neighbors from a node.
     * @param input Node to generate from.
     * @return List of neighbors, and the input required to transition to the state.
     */
    public abstract Iterator<Pair<NodeType, TransitionType>> getNeighbors(NodeType input);
}