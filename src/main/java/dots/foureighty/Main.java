package dots.foureighty;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.players.robots.DelayedGreedyBot;
import dots.foureighty.players.robots.DelayedRandomBot;

public class Main {
    public static void main(String[] args) {
            new GameFactory().withXSize(10).withYSize(10)
                    .withUpdateListener(new GameWatcher())
                    .withBoardGenerator(StandardBoards.AMERICAN)
                    .withPlayer1(new DelayedRandomBot(350))
                    .withPlayer2(new DelayedGreedyBot(350)).build().play();

    }
}