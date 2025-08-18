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
    private final JPanel buttonsPanel = new JPanel();
    private final JPanel waitingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));


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

        buttonsPanel.add(playButton);
        buttonsPanel.add(clearButton);
        footer.add(buttonsPanel, "PLAY");

        waitingPanel.add(new JLabel("Waiting for other player to make move..."));
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
        //TODO: Exit out of this view.
        // Display a different view while waiting for opponent to make a move.
        //this.setEnabled(false);
        showCard("WAIT");
    }

}


