package dots.foureighty.players.robots.algorithms.mcts;

import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveIterator;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.lines.MoveBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;


public class ParallelMCTSAlgorithm {


    public static final class Stats {
        private final AtomicInteger visits = new AtomicInteger();
        private final DoubleAdder reward = new DoubleAdder();


        public void recordSample(double value) {
            // Combine visit increment and reward update to keep updates consistent.
            visits.incrementAndGet();
            reward.add(value);
        }


        public int getVisits() {
            return visits.get();
        }

        public double getMean() {
            int n = visits.get();
            return n == 0 ? 0.0 : reward.sum() / n;
        }
    }


    public static final class Result {
        public final Move bestMove;
        public final Map<Move, Stats> perMove;


        public Result(Move bestMove, Map<Move, Stats> perMove) {
            this.bestMove = bestMove;
            this.perMove = perMove;
        }
    }


    private final Evaluator<DABState> evaluator;
    private final int simulations;
    private final int threads;
    private final int maxRolloutLen;
    private final double explorationCoefficient;


    public ParallelMCTSAlgorithm(Evaluator<DABState> evaluator, int simulations, double explorationC, int threads, int maxRolloutLen) {
        this.evaluator = evaluator;
        this.simulations = simulations;
        this.explorationCoefficient = explorationC;
        this.threads = Math.max(1, threads);
        this.maxRolloutLen = maxRolloutLen;
    }


    /**
     * Runs parallelized MCTS from root state
     * @param root
     * @return result with best move with full move statistics
     */
    public Result search(DABState root) {
        final List<Move> rootMoves = collectRootMoves(root);
        if (rootMoves.isEmpty()) {
            return new Result(null, Collections.emptyMap());
        }

        final ConcurrentHashMap<Move, Stats> statsByMove = new ConcurrentHashMap<>();
        for (Move move : rootMoves) {
            statsByMove.put(move, new Stats());
        }

        final int simulationsPerWorker = (int) Math.ceil((double) simulations / threads);
        final ExecutorService executor = Executors.newFixedThreadPool(threads);
        final List<Callable<Void>> tasks = createTasks(root, rootMoves, statsByMove, simulationsPerWorker);

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        final Move bestMove = chooseBestMove(rootMoves, statsByMove);
        return new Result(bestMove, statsByMove);
    }

    /**
     * Collects all moves from the root state.
     * @param root
     * @return list of legal moves
     */
    private static List<Move> collectRootMoves(DABState root) {
        final List<Move> moves = new ArrayList<>();
        final MoveIterator moveIterator = new MoveIterator(root.getBoard());
        while (moveIterator.hasNext()) {
            moves.add(moveIterator.next());
        }
        return moves;
    }

    /**
     * creates worker tasks for MCTS simulations
     * @param root
     * @param rootMoves
     * @param statsByMove
     * @param simulationsPerWorker
     * @return list of tasks to be executed by executor
     */
    private List<Callable<Void>> createTasks(DABState root,
                                             List<Move> rootMoves,
                                             ConcurrentHashMap<Move, Stats> statsByMove,
                                             int simulationsPerWorker) {
        final List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            tasks.add(() -> {
                final ThreadLocalRandom random = ThreadLocalRandom.current();
                for (int j = 0; j < simulationsPerWorker; j++) {
                    final Move move = selectMoveUcb1(rootMoves, statsByMove, explorationCoefficient, random);
                    final float reward = simulateRollout(root.withMove(move), maxRolloutLen, evaluator, random);
                    statsByMove.get(move).recordSample(reward);
                }
                return null;
            });
        }
        return tasks;
    }

    /**
     * executes single rollout simulation from give state to maxRolloutLen
     * @param state
     * @param maxRolloutLen
     * @param evaluator
     * @param random
     * @return evaluation of final state
     */
    private float simulateRollout(DABState state, int maxRolloutLen, Evaluator<DABState> evaluator, ThreadLocalRandom random) {
        if (maxRolloutLen == 0) return evaluator.evaluate(state);

        int steps = 0;
        while (steps < maxRolloutLen) {
            // Gather moves
            ArrayList<Move> captures = new ArrayList<>();
            ArrayList<Move> safeZero = new ArrayList<>();
            ArrayList<Move> riskyZero = new ArrayList<>();

            MoveIterator it = new MoveIterator(state.getBoard());
            while (it.hasNext()) {
                Move m = it.next();

                // How many boxes would we score if we play m?
                MoveBuilder mb = new MoveBuilder(state.getBoard());
                mb.addAll(m.getLines());
                int boxes = mb.getNewBoxes().length;

                if (boxes > 0) {
                    captures.add(m);
                } else {
                    DABState s2 = state.withMove(m);
                    boolean givesCapture = false;
                    MoveIterator it2 = new MoveIterator(s2.getBoard());
                    while (it2.hasNext()) {
                        Move m2 = it2.next();
                        MoveBuilder mb2 = new MoveBuilder(s2.getBoard());
                        mb2.addAll(m2.getLines());
                        if (mb2.getNewBoxes().length > 0) { givesCapture = true; break; }
                    }
                    if (!givesCapture) safeZero.add(m);
                    else riskyZero.add(m);
                }
            }

            // Terminal?
            if (captures.isEmpty() && safeZero.isEmpty() && riskyZero.isEmpty()) break;

            Move choice;
            if (!captures.isEmpty()) {
                choice = captures.get(random.nextInt(captures.size()));
            } else if (!safeZero.isEmpty()) {
                choice = safeZero.get(random.nextInt(safeZero.size()));
            } else {
                choice = riskyZero.get(random.nextInt(riskyZero.size()));
            }

            state = state.withMove(choice);
            steps++;
        }
        return evaluator.evaluate(state);
    }


    /**
     * selects move via UCB1 selection
     * @param moves
     * @param statsByMove
     * @param explorationCoefficient
     * @param random
     * @return selected move
     */
    private static Move selectMoveUcb1(List<Move> moves,
                                       Map<Move, Stats> statsByMove,
                                       double explorationCoefficient,
                                       ThreadLocalRandom random) {
        // If any move is unvisited, prefer exploring them first (uniformly at random among unvisited).
        final List<Move> unvisited = new ArrayList<>();
        int totalVisits = 0;
        for (Move m : moves) {
            int v = statsByMove.get(m).getVisits();
            totalVisits += v;
            if (v == 0) {
                unvisited.add(m);
            }
        }
        if (!unvisited.isEmpty()) {
            return unvisited.get(random.nextInt(unvisited.size()));
        }


        // All visited: pick by UCB1.
        Move best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Move m : moves) {
            Stats s = statsByMove.get(m);
            double score = ucbScore(s.getMean(), s.getVisits(), totalVisits, explorationCoefficient);
            if (score > bestScore) {
                bestScore = score;
                best = m;
            }
        }
        return best;
    }

    /**
     * calculates UCB1 score for given move
     * @param mean
     * @param visits
     * @param totalVisits
     * @param c
     * @return ucb1
     */
    private static double ucbScore(double mean, int visits, int totalVisits, double c) {
        if (visits == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return mean + c * Math.sqrt(Math.log(Math.max(1, totalVisits)) / visits);
    }

    /**
     * chooses best move from given list of moves by highest mean reward
     * @param moves
     * @param statsByMove
     * @return best move
     */
    private static Move chooseBestMove(List<Move> moves, Map<Move, Stats> statsByMove) {
        // Pick the move with the highest mean; break ties with higher visits.
        Move best = null;
        double bestMean = Double.NEGATIVE_INFINITY;
        int bestVisits = -1;
        for (Move m : moves) {
            Stats s = statsByMove.get(m);
            double mean = s.getMean();
            int visits = s.getVisits();
            if (mean > bestMean || (mean == bestMean && visits > bestVisits)) {
                best = m;
                bestMean = mean;
                bestVisits = visits;
            }
        }
        return best;
    }
}

