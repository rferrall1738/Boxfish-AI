package dots.foureighty;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.players.robots.DelayedGreedyBot;

public class Main {
    public static void main(String[] args) {
            new GameFactory().withXSize(10).withYSize(10)
                    .withUpdateListener(new GameWatcher())
                    .withBoardGenerator(StandardBoards.ICELANDIC)
                    .withPlayer1(new DelayedGreedyBot(250))
                    .withPlayer2(new DelayedGreedyBot(250)).build().play();

    }
}