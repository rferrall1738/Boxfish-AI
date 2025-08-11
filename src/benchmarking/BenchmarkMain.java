import dots.foureighty.game.GameFactory;
import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.searchbots.minimax.MinimaxBot;
import dots.foureighty.players.robots.searchbots.minimax.ParallelMaxBot;

import java.util.LinkedList;

public class BenchmarkMain {

    private static final int RUNS_PER_BOARD = 100;

    public static void main(String[] args) {
        GameFactory factory = new GameFactory()
                .withXSize(5)
                .withYSize(5)
                .withBoardGenerator(StandardBoards.AMERICAN);

        GameSnapshot snapshot = factory.build().generateSnapshot();

        Player minimax = new MinimaxBot(3);
        Player parallel = new ParallelMaxBot(3);


        System.out.println("Benchmarking ParallelMinimaxBot...");
        BotBenchmark parallelBenchmark = benchmarkBot(parallel, snapshot);
        System.out.println(parallelBenchmark);

        System.out.println("Benchmarking MinimaxBot...");

        BotBenchmark minimaxBenchmark = benchmarkBot(minimax, snapshot);
        System.out.println(minimaxBenchmark);
    }

    /**
     * loops through how many runs you want to test
     *
     * @param bot
     * @param snapshot
     */
    private static BotBenchmark benchmarkBot(Player bot, GameSnapshot snapshot) {
        LinkedList<Long> times = new LinkedList<>();

        for (int i = 0; i < RUNS_PER_BOARD; i++) {
            long start = System.nanoTime();
            bot.getMove(snapshot);
            long end = System.nanoTime();
            times.add((end - start) / 1_000_000); // convert to mS
        }
        return new BotBenchmark(times);
    }

}
