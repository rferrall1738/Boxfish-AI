package dots.foureighty;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;
import dots.foureighty.gui.StartScreen;

public class Main {
    public static void main(String[] args) {
        // Uncomment for main screen
        //new StartScreen();

        new GameFactory().withXSize(7).withYSize(7)
                    .withUpdateListener(new GameWatcher())
                    .withBoardGenerator(StandardBoards.AMERICAN)
                .withPlayer2(new GreedyBot())
                .withPlayer1(new AlphaBetaBot(3))
                .build().play();
    }

}