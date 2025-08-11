package dots.foureighty.players.robots.algorithms.minimax;

import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public abstract class ParallelMinimaxSearchAlgorithm<InputType, TransitionType> extends MinimaxSearchAlgorithm<InputType, TransitionType> {

    protected Pair<LinkedList<TransitionType>, Float> search(InputType input, NeighborGenerator neighborGenerator, Evaluator evaluator, int depth, boolean maximize) {

        // creates a list of tasks to be executed by the executor
        List<Callable<Pair<TransitionType, Pair<LinkedList<TransitionType>, Float>>>> tasks = new LinkedList<>();

        Iterator<Pair<InputType, TransitionType>> neighborIterator = neighborGenerator.getNeighbors(input);

        //If endstate or ran out of depth, evaluate the current position.
        if (!neighborIterator.hasNext() || depth == 0) {
            return new Pair<>(new LinkedList<>(), evaluator.evaluate(input));
        }

        //for every move create a task and evaluate
        while (neighborIterator.hasNext()) {
            Pair<InputType, TransitionType> neighbor = neighborIterator.next();

            // creates a task that performs minimax on the child state
            tasks.add(() -> {
                Pair<LinkedList<TransitionType>, Float> result = search(neighbor.getKey(), neighborGenerator, evaluator, depth - 1, !maximize);
                return new Pair<>(neighbor.getValue(), result);
            });
        }

        //create thread pool with all available processors
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Pair<LinkedList<TransitionType>, Float> bestChild = new Pair<>(null, maximize ? -Float.MAX_VALUE : Float.MAX_VALUE);
        TransitionType bestTransition = null;

        try {
            List<Future<Pair<TransitionType, Pair<LinkedList<TransitionType>, Float>>>> results = executor.invokeAll(tasks);
            //chose the result with the highest score
            for (Future<Pair<TransitionType, Pair<LinkedList<TransitionType>, Float>>> future : results) {
                Pair<TransitionType, Pair<LinkedList<TransitionType>, Float>> result = future.get();
                if (result.getValue().getValue() > bestChild.getValue()) {
                    bestChild = result.getValue();
                    bestTransition = result.getKey();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        LinkedList<TransitionType> transitionList = new LinkedList<>();
        transitionList.add(bestTransition);
        if (bestChild.getKey() != null)
            transitionList.addAll(bestChild.getKey());

        return new Pair<>(transitionList, bestChild.getValue());

    }
}
