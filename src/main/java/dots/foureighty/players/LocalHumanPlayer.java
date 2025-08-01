package dots.foureighty.players;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.gui.ui.MoveSubmissionPanel;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.packages.MovePackage;

import javax.swing.*;
import java.awt.*;

public class LocalHumanPlayer implements Player {
    private final String name;
    private Color color;
    public LocalHumanPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * @return
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * @param newColor
     */
    @Override
    public void setColor(final Color newColor) {
        this.color = newColor == null ? newColor : new Color(newColor.getRGB() | 0x07);
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return name;
    }


    @Override
    public Move getMove(GameSnapshot gameState) {
        final MovePackage  movePackage = new MovePackage();
            new Thread(() -> {
                final JFrame inputFrame = new JFrame("Your turn!");
                inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                MoveSubmissionPanel userInputPanel = new MoveSubmissionPanel(gameState, (move -> {
                    movePackage.setMove(move);
                    synchronized (movePackage) {
                        movePackage.notifyAll();
                    }
                    synchronized (inputFrame) {
                        inputFrame.setVisible(false);
                        inputFrame.dispose();
                    }
                }));
                inputFrame.add(userInputPanel);
                inputFrame.setVisible(true);
                inputFrame.pack();

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
}
