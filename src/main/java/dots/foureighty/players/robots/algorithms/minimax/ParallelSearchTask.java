package dots.foureighty.players.robots.algorithms.minimax;

import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.RecursiveTask;

public class ParallelSearchTask<InputType, TransitionType> extends RecursiveTask<Pair<LinkedList<TransitionType>, Float>> {
    private final InputType input;
    private final NeighborGenerator<InputType, TransitionType> neighborGenerator;
    private final LinkedList<TransitionType> parentTransitions;
    private final Evaluator<InputType> evaluator;
    private final int depth;
    private final boolean maximize;

    private final MinimaxSearchAlgorithm<InputType, TransitionType> minimaxSearchAlgorithm = new MinimaxSearchAlgorithm<>();

    public ParallelSearchTask(InputType input, LinkedList<TransitionType> parentTransitions, NeighborGenerator<InputType, TransitionType> neighborGenerator, Evaluator<InputType> evaluator, int depth, boolean maximize) {
        this.input = input;
        this.parentTransitions = parentTransitions;
        this.neighborGenerator = neighborGenerator;
        this.evaluator = evaluator;
        this.depth = depth;
        this.maximize = maximize;
    }

    @Override
    protected Pair<LinkedList<TransitionType>, Float> compute() {
        if (depth <= 1) {
            Pair<LinkedList<TransitionType>, Float> res = minimaxSearchAlgorithm.search(input, neighborGenerator, evaluator, depth, maximize);
            parentTransitions.addAll(res.getKey());
            return new Pair<>(parentTransitions, res.getValue());
        }
        Iterator<Pair<InputType, TransitionType>> neighbors = neighborGenerator.getNeighbors(input);

        if (!neighbors.hasNext()) {
            return new Pair<>(parentTransitions, evaluator.evaluate(input));
        }

        LinkedList<ParallelSearchTask<InputType, TransitionType>> searchTasks = new LinkedList<>();
        while (neighbors.hasNext()) {
            Pair<InputType, TransitionType> neighbor = neighbors.next();
            LinkedList<TransitionType> transitionList = new LinkedList<>(parentTransitions);
            transitionList.addLast(neighbor.getValue());
            searchTasks.add(new ParallelSearchTask<>(neighbor.getKey(), transitionList, neighborGenerator, evaluator, depth - 1, !maximize));
        }

        LinkedList<Pair<LinkedList<TransitionType>, Float>> children = new LinkedList<>();
        searchTasks.stream().map(ParallelSearchTask::fork).forEach(forkTask -> children.add(forkTask.join()));

        Pair<LinkedList<TransitionType>, Float> bestChild = new Pair<>(null, maximize ? -Float.MAX_VALUE : Float.MAX_VALUE);

        for (Pair<LinkedList<TransitionType>, Float> child : children) {
            if (maximize) {
                if (child.getValue() > bestChild.getValue()) {
                    bestChild = child;
                }
            } else {
                if (child.getValue() < bestChild.getValue()) {
                    bestChild = child;
                }
            }

        }
        return bestChild;
    }
}
