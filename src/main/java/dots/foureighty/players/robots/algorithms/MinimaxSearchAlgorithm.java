package dots.foureighty.players.robots.algorithms;

import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;

public class MinimaxSearchAlgorithm<InputType, TransitionType> extends SearchAlgorithm<InputType, TransitionType> {
    protected int nodesVisited=0;
    protected int nodesPruned= 0;

    public int getNodesVisited(){return nodesVisited;}

    public int getNodesPruned(){return nodesPruned;}

    @Override
    protected final Pair<LinkedList<TransitionType>, Float> search(InputType input, NeighborGenerator neighborGenerator, Evaluator evaluator) {
        return search(input, neighborGenerator, evaluator, -1, true, -Float.MAX_VALUE, Float.MAX_VALUE);
    }

    protected final Pair<LinkedList<TransitionType>, Float> search(InputType input, NeighborGenerator neighborGenerator, Evaluator evaluator, int depth, boolean maximize, float alpha, float beta) {
        Iterator<Pair<InputType, TransitionType>> neighbors = neighborGenerator.getNeighbors(input);
        if (!neighbors.hasNext() || depth == 0) {
            nodesVisited++;
            return new Pair<>(new LinkedList<>(), evaluator.evaluate(input));
        }
        Pair<LinkedList<TransitionType>, Float> bestChild = new Pair<>(null, maximize ? -Float.MAX_VALUE : Float.MAX_VALUE);
        TransitionType bestTransition = null;

        while (neighbors.hasNext()) {
            nodesVisited++;
            Pair<InputType, TransitionType> neighbor = neighbors.next();

            Pair<LinkedList<TransitionType>, Float> child = search(neighbor.getKey(), neighborGenerator, evaluator, depth - 1, !maximize,alpha, beta);
            if (maximize) {
                if (child.getValue() > bestChild.getValue()) {
                    bestChild = child;
                    bestTransition = neighbor.getValue();
                }
                alpha = Math.max(alpha, child.getValue());
                if (beta <= alpha) {
                    nodesPruned++;
                    break;
                }
            } else {
                if (child.getValue() < bestChild.getValue()) {
                    bestChild = child;
                    bestTransition = neighbor.getValue();
                }
                beta = Math.min(beta, child.getValue());
                if (beta <= alpha){
                    nodesPruned++;
                    break;
                }
            }
        }

        LinkedList<TransitionType> transitionList = new LinkedList<>();
        transitionList.add(bestTransition);
        if (bestChild.getKey() != null)
            transitionList.addAll(bestChild.getKey());

        return new Pair<>(transitionList, bestChild.getValue());
    }
}
