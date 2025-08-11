package dots.foureighty.players.robots.searchbots.minimax;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveIterator;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelMaxBot extends MinimaxBot{
    public ParallelMaxBot(int depth, Heuristic<MinimaxState>... heuristics){
        super(depth, heuristics);
    }

    @Override
    public Move getMove(GameSnapshot gameState){
        MinimaxState initialState = new MinimaxState(gameState.getBoard());

        final MoveIterator moveIterator = new MoveIterator(initialState.getBoard());


        // creates a list of tasks to be executed by the executor
        List<Callable<Pair<Move, Float>>> tasks = new ArrayList<>();

        //for every move create a task and evaluate
        while(moveIterator.hasNext()){
            Move move = moveIterator.next();

            // creates a task that performs minimax on the child state
            tasks.add(() -> {
                MinimaxState childState = initialState.withMove(move);
                Pair<LinkedList<Move>,Float> result = search(childState, getNeighborGenerator(),getEvaluator(), getDepth() -1, false);
                return new Pair<>(move, result.getValue());
            });
        }

        //create thread pool with all available processors
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Move bestMove = null;
        float bestScore = -Float.MAX_VALUE;

        try{
            List<Future<Pair<Move, Float>>> results = executor.invokeAll(tasks);
            //chose the result with highest score
            for(Future<Pair<Move, Float>> future : results){
                Pair<Move,Float> result = future.get();
                if(result.getValue() > bestScore){
                    bestScore = result.getValue();
                    bestMove = result.getKey();
                }
            }
        }
        catch (InterruptedException| ExecutionException e){
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }
        return bestMove;
    }

    private int getDepth(){ return super.depth; }

    private Evaluator getEvaluator(){ return super.stateEvaluator;}

    private NeighborGenerator getNeighborGenerator(){ return super.neighborGenerator;}

    @Override
    public String getName(){return "ParallelMiniMaxBot (" + depth + ")";}

}
