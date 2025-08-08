package dots.foureighty;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.heuristics.ChainHeuristic;
import dots.foureighty.players.robots.heuristics.GreedyCompleteBox;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;
import dots.foureighty.players.robots.searchbots.minimax.ParallelMaxBot;

public class Main {
    public static void main(String[] args) {
        new GameFactory().withXSize(5).withYSize(5)
                    .withUpdateListener(new GameWatcher())
                    .withBoardGenerator(StandardBoards.AMERICAN)
                .withPlayer1(new ParallelMaxBot(3))
                .withPlayer2(new GreedyBot()).build().play();
    }

}