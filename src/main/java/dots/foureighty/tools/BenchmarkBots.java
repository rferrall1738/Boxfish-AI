
package dots.foureighty.tools;

import dots.foureighty.game.GameFactory;
import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.heuristics.ChainHeuristic;
import dots.foureighty.players.robots.heuristics.GreedyCompleteBox;
import dots.foureighty.players.robots.searchbots.minimax.MinimaxBot;
import dots.foureighty.players.robots.searchbots.minimax.ParallelMaxBot;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkBots {

    /**
     * Main method for running benchmarks.
     * @param args
     */
    public static void main(String[] args) {
        GameFactory factory = new GameFactory()
                .withXSize(5)
                .withYSize(5)
                .withBoardGenerator(StandardBoards.AMERICAN);

        GameSnapshot snapshot = factory.build().generateSnapshot();

        Player minimax = new MinimaxBot(4, new ChainHeuristic(), new GreedyCompleteBox());
        Player parallel = new ParallelMaxBot(4, new ChainHeuristic(), new GreedyCompleteBox());

        System.out.println("Benchmarking MinimaxBot...");
        benchmarkBot(minimax, snapshot);

        System.out.println("\nBenchmarking ParallelMinimaxBot...");
        benchmarkBot(parallel, snapshot);
    }

    /**
     * loops through how many runs you want to test
     * @param bot
     * @param snapshot
     */
    private static void benchmarkBot(Player bot, GameSnapshot snapshot) {
        int runs = 100;
        List<Long> times = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            bot.getMove(snapshot);
            long end = System.nanoTime();
            times.add((end - start) / 1_000_000); // convert to ms
        }

        long min = times.stream().min(Long::compareTo).orElse(0L);
        long max = times.stream().max(Long::compareTo).orElse(0L);
        double avg = times.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double stddev = calculateStdDev(times, avg);

        System.out.printf("Runs: %d\n", runs);
        System.out.printf("Average: %.2f ms\n", avg);
        System.out.printf("Min: %d ms\n", min);
        System.out.printf("Max: %d ms\n", max);
        System.out.printf("Std Dev: %.2f ms\n", stddev);
    }

    /**
     * Calculates the standard deviation of a list of times.
     * @param times
     * @param mean
     * @return stdev
     */
    private static double calculateStdDev(List<Long> times, double mean) {
        double sumSquaredDiffs = 0;
        for (long time : times) {
            sumSquaredDiffs += Math.pow(time - mean, 2);
        }
        return Math.sqrt(sumSquaredDiffs / times.size());
    }
}
