package dots.foureighty.players.robots.algorithms.minimax;

import dots.foureighty.players.robots.algorithms.SearchAlgorithm;
import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;

public class MinimaxSearchAlgorithm<InputType, TransitionType> extends SearchAlgorithm<InputType, TransitionType> {

    @Override
    protected final Pair<LinkedList<TransitionType>, Float> search(InputType input, NeighborGenerator neighborGenerator, Evaluator evaluator) {
        return search(input, neighborGenerator, evaluator, -1, true);
    }

    protected Pair<LinkedList<TransitionType>, Float> search(InputType input, NeighborGenerator neighborGenerator, Evaluator evaluator, int depth, boolean maximize) {
        Iterator<Pair<InputType, TransitionType>> neighbors = neighborGenerator.getNeighbors(input);
        if (!neighbors.hasNext() || depth == 0) {
            return new Pair<>(new LinkedList<>(), evaluator.evaluate(input));
        }
        Pair<LinkedList<TransitionType>, Float> bestChild = new Pair<>(null, maximize ? -Float.MAX_VALUE : Float.MAX_VALUE);
        TransitionType bestTransition = null;

        while (neighbors.hasNext()) {
            Pair<InputType, TransitionType> neighbor = neighbors.next();

            Pair<LinkedList<TransitionType>, Float> child = search(neighbor.getKey(), neighborGenerator, evaluator, depth - 1, !maximize);
            if (maximize) {
                if (child.getValue() > bestChild.getValue()) {
                    bestChild = child;
                    bestTransition = neighbor.getValue();
                }
            } else {
                if (child.getValue() < bestChild.getValue()) {
                    bestChild = child;
                    bestTransition = neighbor.getValue();
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
