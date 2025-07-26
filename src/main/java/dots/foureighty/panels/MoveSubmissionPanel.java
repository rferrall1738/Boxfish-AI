package dots.foureighty.panels;

import dots.foureighty.gamebuilder.Game;
import dots.foureighty.players.Move;

import javax.swing.*;
import java.awt.*;

public class MoveSubmissionPanel extends JPanel {
    private final MoveHandler moveHandler;
    private final JButton playButton;
    private final JButton clearButton;
    private final InteractableGamePanel interactableGamePanel;

    private final InteractableGamePanel.MoveStatusListener  moveStatusListener = new  InteractableGamePanel.MoveStatusListener() {
        @Override
        public void handleStatusUpdate() {
            playButton.setEnabled(interactableGamePanel.isMoveComplete());
            clearButton.setEnabled(interactableGamePanel.getNewLines().size()>0);
        }
    };
    public MoveSubmissionPanel(Game game, MoveHandler moveHandler) {
        this.moveHandler = moveHandler;
        setLayout(new BorderLayout());
        this.interactableGamePanel = new InteractableGamePanel(game, moveStatusListener);
        add(this.interactableGamePanel, BorderLayout.CENTER);



        this.playButton = new JButton("Play Move");
        playButton.setEnabled(false);
        playButton.addActionListener((e) -> this.submitMove());
        this.clearButton = new JButton("Clear Move");
        clearButton.setEnabled(false);

        clearButton.addActionListener((e) -> this.clearQueuedMove());
        JPanel buttons = new JPanel();

        buttons.add(playButton);
        buttons.add(clearButton);

        add(buttons, BorderLayout.PAGE_END);
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
        this.setEnabled(false);
    }

}
