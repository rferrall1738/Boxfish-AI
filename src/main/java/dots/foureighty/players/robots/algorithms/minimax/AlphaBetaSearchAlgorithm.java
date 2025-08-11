package dots.foureighty.players.robots.algorithms.minimax;

import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

import java.util.LinkedList;

public abstract class AlphaBetaSearchAlgorithm<InputType, TransitionType> extends MinimaxSearchAlgorithm<InputType, TransitionType> {


    public abstract class SkippableNeighborGenerator extends NeighborGenerator<InputType, TransitionType> {
        @Override
        public abstract SkippableIterator<Pair<InputType, TransitionType>> getNeighbors(InputType input);
    }

    @Override
    protected Pair<LinkedList<TransitionType>, Float> search(InputType input, NeighborGenerator<InputType, TransitionType> neighborGenerator, Evaluator<InputType> evaluator, int depth, boolean maximize) {
        return search(input, (SkippableNeighborGenerator) neighborGenerator, evaluator, depth, maximize, -Float.MAX_VALUE, Float.MAX_VALUE);
    }

    protected final Pair<LinkedList<TransitionType>, Float> search(InputType input, SkippableNeighborGenerator neighborGenerator, Evaluator evaluator, int depth, boolean maximize, float alpha, float beta) {
        SkippableIterator<Pair<InputType, TransitionType>> neighbors = neighborGenerator.getNeighbors(input);

        if (!neighbors.hasNext() || depth == 0) {
            return new Pair<>(new LinkedList<>(), evaluator.evaluate(input));
        }

        Pair<LinkedList<TransitionType>, Float> bestChild = new Pair<>(null, maximize ? -Float.MAX_VALUE : Float.MAX_VALUE);
        TransitionType bestTransition = null;

        while (neighbors.hasNext()) {

            Pair<InputType, TransitionType> neighbor = neighbors.next();

            Pair<LinkedList<TransitionType>, Float> child = search(neighbor.getKey(), neighborGenerator, evaluator, depth - 1, !maximize, alpha, beta);
            if (maximize) {
                if (child.getValue() > bestChild.getValue()) {
                    bestChild = child;
                    bestTransition = neighbor.getValue();
                }
                alpha = Math.max(alpha, child.getValue());
            } else {
                if (child.getValue() < bestChild.getValue()) {
                    bestChild = child;
                    bestTransition = neighbor.getValue();
                }
                beta = Math.min(beta, child.getValue());
            }
            if (beta <= alpha) {
                neighbors.pruneCurrentBranch();
                break;
            }
        }

        LinkedList<TransitionType> transitionList = new LinkedList<>();
        transitionList.add(bestTransition);
        if (bestChild.getKey() != null)
            transitionList.addAll(bestChild.getKey());

        return new Pair<>(transitionList, bestChild.getValue());
    }
}
