package dots.foureighty.players.robots.algorithms;

import dots.foureighty.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;

public class MinimaxSearchAlgorithm<InputType, TransitionType> extends SearchAlgorithm<InputType, TransitionType> {

    @Override
    protected Pair<TransitionType[], Integer> search(InputType input, NeighborGenerator neighborGenerator, Evaluator evaluator) {
        return search(input, neighborGenerator, evaluator, -1, true);
    }

    protected Pair<TransitionType[], Integer> search(InputType input, NeighborGenerator neighborGenerator, Evaluator evaluator, int depth, boolean maximize) {
        Pair<InputType, TransitionType>[] neighbors = neighborGenerator.getNeighbors(input);
        if (neighbors.length == 0 || depth == 0) {
            return new Pair<>(null, evaluator.evaluate(input));
        }
        Pair<TransitionType[], Integer> bestChild = new Pair<>(null, maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE);
        TransitionType bestTransition = null;
        for (Pair<InputType, TransitionType> neighbor : neighbors) {
            Pair<TransitionType[], Integer> child = search(neighbor.getKey(), neighborGenerator, evaluator, depth - 1, !maximize);
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
            transitionList.addAll(Arrays.asList(bestChild.getKey()));


        return new Pair<>((TransitionType[]) transitionList.toArray(), bestChild.getValue());
    }
}
