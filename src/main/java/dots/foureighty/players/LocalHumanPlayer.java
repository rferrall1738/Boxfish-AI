package dots.foureighty.players;

import dots.foureighty.Main;
import dots.foureighty.gamebuilder.Game;
import dots.foureighty.panels.MovePackage;
import dots.foureighty.panels.MoveSubmissionPanel;

import java.awt.*;

public class LocalHumanPlayer implements LinePlayer {
    private final String playerName;
    private final Color playerColor;

    public LocalHumanPlayer(String playerName, Color playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;

    }
    @Override
    public Move getMove(Game gameState) {
        final MovePackage  movePackage = new MovePackage();
            new Thread(() -> {
                Main.MAIN_FRAME.getContentPane().removeAll();
                    Main.MAIN_FRAME.add(new MoveSubmissionPanel(gameState, (move -> {
                        movePackage.setMove(move);
                        synchronized (movePackage) {
                            movePackage.notifyAll();
                        }
                    })));
                Main.MAIN_FRAME.setVisible(true);
                Main.MAIN_FRAME.pack();
            }).start();

            try {
                synchronized (movePackage) {
                 movePackage.wait();
                 return movePackage.getMove();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public String getName() {
        return playerName;
    }

    @Override
    public Color getColor() {
        return playerColor;
    }
}
