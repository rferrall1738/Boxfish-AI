package dots.foureighty.players.robots.algorithms.minimax;

import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;

public abstract class ParallelMinimaxSearchAlgorithm<InputType, TransitionType> extends MinimaxSearchAlgorithm<InputType, TransitionType> {

    protected Pair<LinkedList<TransitionType>, Float> search(InputType input, NeighborGenerator<InputType, TransitionType> neighborGenerator, Evaluator<InputType> evaluator, int depth, boolean maximize) {

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ParallelSearchTask<InputType, TransitionType> task = new ParallelSearchTask<>(input, new LinkedList<>(), neighborGenerator, evaluator, depth, maximize);
        return forkJoinPool.invoke(task);
    }
}
