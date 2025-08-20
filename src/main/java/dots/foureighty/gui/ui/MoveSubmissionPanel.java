package dots.foureighty.gui.ui;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;

import javax.swing.*;
import java.awt.*;

public class MoveSubmissionPanel extends JPanel {
    private final MoveHandler moveHandler;
    private final JButton playButton;
    private final JButton clearButton;
    private final InteractableGamePanel interactableGamePanel;
    private final JPanel footer = new JPanel(new CardLayout());


    public MoveSubmissionPanel(GameSnapshot game, MoveHandler moveHandler) {
        this.moveHandler = moveHandler;
        setLayout(new BorderLayout());
        InteractableGamePanel.MoveStatusListener moveStatusListener = new InteractableGamePanel.MoveStatusListener() {
            @Override
            public void handleStatusUpdate() {
                playButton.setEnabled(interactableGamePanel.getMoveBuilder().isComplete());
                clearButton.setEnabled(!interactableGamePanel.getNewLines().isEmpty());
            }
        };
        this.interactableGamePanel = new InteractableGamePanel(game, moveStatusListener);
        add(this.interactableGamePanel, BorderLayout.CENTER);

        this.playButton = new JButton("Play Move");
        playButton.setEnabled(false);
        playButton.addActionListener((e) -> this.submitMove());
        this.clearButton = new JButton("Clear Move");
        clearButton.setEnabled(false);
        clearButton.addActionListener((e) -> this.clearQueuedMove());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(playButton);
        buttonsPanel.add(clearButton);
        footer.add(buttonsPanel, "PLAY");

        JLabel waitingText = new JLabel();

        Thread waitingAnimation = new Thread(() -> {
            for(int i = 0; i == i; i = (i + 1) % 5) {
                StringBuilder sb = new StringBuilder("Waiting for other player to make move");
                for (int j = 0; j < i; j++) {
                    sb.append(".");
                }
                waitingText.setText(sb.toString());
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    System.exit(1);
                }
            }
        });
        JPanel waitingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        waitingPanel.add(waitingText);
        waitingAnimation.start();
        footer.add(waitingPanel, "WAIT");

        add(footer, BorderLayout.PAGE_END);

        showCard("PLAY");
    }
    private void showCard(String name) {
        ((CardLayout) footer.getLayout()).show(footer, name);
    }
    public void clearQueuedMove() {
        this.interactableGamePanel.clearQueuedMove();
    }
    public void submitMove() {
        this.moveHandler.playMove(new Move(interactableGamePanel.getNewLines()));
        playButton.setEnabled(false);
        clearButton.setEnabled(false);
        interactableGamePanel.setEnabled(false);
        showCard("WAIT");
    }

}


