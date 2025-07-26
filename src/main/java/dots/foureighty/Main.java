package dots.foureighty;


import dots.foureighty.game.GameFactory;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.players.robots.DelayedRandomBot;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new GameFactory().withXSize(10).withYSize(10)
                    .withUpdateListener(new GameWatcher())
                    .withPlayer1(new DelayedRandomBot())
                    .withPlayer2(new DelayedRandomBot()).build().play();
        }

    }
}