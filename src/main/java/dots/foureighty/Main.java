package dots.foureighty;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.heuristics.GreedyCompleteBox;
import dots.foureighty.players.robots.heuristics.chainHeuristic;
import dots.foureighty.players.robots.searchbots.minimax.MinimaxBot;

public class Main {
    public static void main(String[] args) {
        new GameFactory().withXSize(4).withYSize(4)
                    .withUpdateListener(new GameWatcher())
                    .withBoardGenerator(StandardBoards.AMERICAN)
                .withPlayer1(new GreedyBot())
                .withPlayer2(new MinimaxBot(4, new GreedyCompleteBox(), new chainHeuristic())).build().play();
    }

}