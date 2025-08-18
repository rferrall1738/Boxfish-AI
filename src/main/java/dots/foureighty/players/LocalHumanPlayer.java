package dots.foureighty.players;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.gui.ui.MoveSubmissionPanel;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.packages.MovePackage;
import dots.foureighty.listeners.GameUpdateListener;
import dots.foureighty.listeners.GameUpdateType;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class LocalHumanPlayer implements Player, GameUpdateListener  {
    private final String name;
    private Color color;
    private final JFrame frame = new JFrame("Your turn!");
    private boolean firstShow = true;
    private MoveSubmissionPanel panel;
    private volatile boolean gameOver = false;
    private volatile CountDownLatch inFlightLatch;

    public LocalHumanPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationByPlatform(true);
        });
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
        if (gameOver) {
            return null;
        }
        final AtomicReference<Move> result = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        inFlightLatch = latch;

        try {
            SwingUtilities.invokeAndWait(() -> {
                Dimension oldSize = frame.isShowing() ? frame.getSize() : null;
                Point oldLoc = frame.isShowing() ? frame.getLocation() : null;

                if (panel != null) frame.getContentPane().remove(panel);
                panel = new MoveSubmissionPanel(gameState, move -> {
                    result.set(move);
                    latch.countDown();
                    frame.setTitle("Waiting…");
                    panel.setEnabled(false);
                });
                frame.getContentPane().add(panel);

                if (firstShow) {
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    firstShow = false;
                } else {
                    frame.getContentPane().revalidate();
                    frame.repaint();
                    if (oldSize != null) frame.setSize(oldSize);
                    if (oldLoc != null) frame.setLocation(oldLoc);
                }

                if (!frame.isVisible()) frame.setVisible(true);
                frame.setTitle(name + " – Your turn");
                panel.setEnabled(true);
            });
        } catch (Exception e) {
            inFlightLatch = null;
            throw new RuntimeException(e);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            inFlightLatch = null;
        }
        if (gameOver) {
            return null;
        }
        return result.get();
    }
    @Override
    public void handleUpdate(GameSnapshot gameSnapshot, GameUpdateType type) {
        if (type == GameUpdateType.GAME_END) {
            onGameOver();
        }
    }

    private void onGameOver() {
        gameOver = true;

        CountDownLatch latch = inFlightLatch;
        if (latch != null) {
            latch.countDown();
        }

        SwingUtilities.invokeLater(() -> {
            if (frame.isDisplayable()) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
}


