package dots.foureighty.gui;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.listeners.GameUpdateListener;
import dots.foureighty.listeners.GameUpdateType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWatcher extends JFrame implements GameUpdateListener {
    private GameSnapshot game;

    private final JPanel playersPanel = new JPanel();
    private final JPanel player1Panel = new JPanel();
    private final JPanel player2Panel = new JPanel();
    private final JLabel player1Name = new JLabel();
    private final JLabel player2Name = new JLabel();
    private final JLabel player1Score = new JLabel();
    private final JLabel player2Score = new JLabel();

    private GamePanel gamePanel;
    private boolean registered = true;

    public GameWatcher() {
        super("GameWatcher");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                deregister();

                setVisible(false);
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());

        playersPanel.setLayout(new GridLayout(1, 2));

        player1Panel.setLayout(new GridLayout(2, 1));
        player2Panel.setLayout(new GridLayout(2, 1));

        playersPanel.add(player1Panel, 0, 0);
        playersPanel.add(player2Panel, 0, 1);
        player1Panel.add(player1Name, 0, 0);
        player1Panel.add(player1Score, 1, 0);

        player2Panel.removeAll();
        player2Panel.add(player2Name, 0, 0);
        player2Panel.add(player2Score, 1, 0);


    }

    private void updatePlayerPanels() {
        player1Name.setText(game.getPlayer1Name());
        player2Name.setText(game.getPlayer2Name());
        player1Score.setText(game.getPlayer1Boxes().length + "");
        player2Score.setText(game.getPlayer2Boxes().length + "");
    }

    public void deregister() {
        if (registered && game != null) {
            registered = false;
        }
    }

    public void begin() {
        gamePanel = new GamePanel(game);

        updatePlayerPanels();
        getContentPane().add(gamePanel, BorderLayout.CENTER);
        getContentPane().add(playersPanel, BorderLayout.PAGE_START);

        setVisible(true);
        pack();
    }

    public void updateGame(GameSnapshot gameSnapshot) {
        this.game = gameSnapshot;
        gamePanel.updateGame(gameSnapshot);

        updatePlayerPanels();

    }

    @Override
    public boolean unregister() {
        return !registered;
    }

    @Override
    public void handleUpdate(GameSnapshot gameSnapshot, GameUpdateType gameUpdateType) {

        if (gameUpdateType == GameUpdateType.GAME_START) {
            this.game = gameSnapshot;
            begin();
            return;
        }

        if (gameUpdateType == GameUpdateType.MOVE_PLAYED) {
            updateGame(gameSnapshot);
        }
    }
}
