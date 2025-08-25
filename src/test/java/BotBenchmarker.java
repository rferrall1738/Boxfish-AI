import botbenchmark.BotStats;
import botbenchmark.LockableInt;
import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.Board;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.listeners.GameUpdateType;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.searchbots.mcts.IterationsBasedMCTSBot;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;
import dots.foureighty.util.Pair;

import java.util.LinkedList;

public class BotBenchmarker {

private static Pair<Integer,Integer> runTrial(Player player1, Player player2, Board board) throws InterruptedException {

        LockableInt p1Boxes = new  LockableInt(0);

        new GameFactory().withPlayer1(player1).withPlayer2(player2)
                .withBoard(board).withUpdateListener((game, event) -> {
                    if (event == GameUpdateType.GAME_END) {
                        synchronized (p1Boxes) {
                            p1Boxes.setValue(game.getPlayer1Boxes().length);
                            p1Boxes.notifyAll();
                        }
                    }
                }).build().play();
        synchronized (p1Boxes) {
            p1Boxes.wait();
        }
        return new Pair<>(p1Boxes.getValue(), board.getBoxes().size() - p1Boxes.getValue());
    }

    public static BotStats test(Player player1, Player player2, int numberOfGames, Board board) throws InterruptedException {
        LinkedList<Integer> p1Boxes = new LinkedList<>();
        LinkedList<Integer> p2Boxes = new LinkedList<>();
        for (int i = 0; i < numberOfGames; i++) {

            Pair<Integer,Integer> results  = runTrial(player1,player2, board);
            p1Boxes.add(results.getKey());
            p2Boxes.add(results.getValue());
        }

        return new BotStats(numberOfGames, p1Boxes.stream().mapToInt(Integer::intValue).toArray(),
                p2Boxes.stream().mapToInt(Integer::intValue).toArray());

    }



    public static void main(String[] args) throws InterruptedException {
        Board board = StandardBoards.AMERICAN.generateBoard(5,5);
        Player player1 = new IterationsBasedMCTSBot(100);
        Player player2 = new AlphaBetaBot(2);

        BotStats stats = test(player1, player2, 1, board);

        System.out.println(stats.getP1BoxesStats());
        System.out.println(stats.getP2BoxesStats());
    }
}
