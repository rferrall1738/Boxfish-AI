package dots.foureighty.gui;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.listeners.GameUpdateListener;
import dots.foureighty.listeners.GameUpdateType;
import dots.foureighty.gui.ui.MusicPlayerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWatcher extends JFrame implements GameUpdateListener {
    private GameSnapshot game;

    private final JPanel playersPanel = new JPanel();
    private final JLabel player1Name = new JLabel();
    private final JLabel player2Name = new JLabel();
    private final JLabel player1Score = new JLabel();
    private final JLabel player2Score = new JLabel();
    private final JLabel winnerLabel = new JLabel(" ", SwingConstants.CENTER);

    private GamePanel gamePanel;
    private boolean registered = true;

    public GameWatcher() {
        super("GameWatcher");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());

        playersPanel.setLayout(new GridLayout(1, 2));

        JPanel player1Panel = new JPanel();
        player1Panel.setLayout(new GridLayout(2, 1));
        JPanel player2Panel = new JPanel();
        player2Panel.setLayout(new GridLayout(2, 1));

        playersPanel.add(player1Panel, 0, 0);
        playersPanel.add(player2Panel, 0, 1);
        player1Panel.add(player1Name, 0, 0);
        player1Panel.add(player1Score, 1, 0);

        player2Panel.removeAll();
        player2Panel.add(player2Name, 0, 0);
        player2Panel.add(player2Score, 1, 0);

        winnerLabel.setFont(winnerLabel.getFont().deriveFont(Font.BOLD, 14f));

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(playersPanel, BorderLayout.NORTH);
        topContainer.add(winnerLabel, BorderLayout.SOUTH);
    }

    private void updatePlayerPanels() {
        player1Name.setText(game.getPlayer1Name());
        player1Name.setForeground(game.getPlayer1Color());
        player2Name.setText(game.getPlayer2Name());
        player2Name.setForeground(game.getPlayer2Color());
        player1Score.setText(game.getPlayer1Boxes().length + "");
        player2Score.setText(game.getPlayer2Boxes().length + "");

        String p1FontFamily = player1Name.getFont().getFamily();
        int p1FontSize = player1Name.getFont().getSize();

        String p2FontFamily = player2Name.getFont().getFamily();
        int p2FontSize = player2Name.getFont().getSize();

        if (game.isPlayer1Turn()) {
            player1Name.setFont(new Font(p1FontFamily, Font.BOLD, p1FontSize));
            player2Name.setFont(new Font(p2FontFamily, Font.PLAIN, p2FontSize));
        } else {
            player1Name.setFont(new Font(p1FontFamily, Font.PLAIN, p1FontSize));
            player2Name.setFont(new Font(p2FontFamily, Font.BOLD, p2FontSize));
        }
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

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(playersPanel, BorderLayout.NORTH);
        topContainer.add(winnerLabel, BorderLayout.SOUTH);
        getContentPane().add(topContainer, BorderLayout.PAGE_START);

        // adds music player to bottom of screen
        try {
            MusicPlayerPanel musicPanel = new MusicPlayerPanel(this.getClass().getResource("/dots/foureighty/music/DotsAndBoxes.wav"));
            getContentPane().add(musicPanel, BorderLayout.PAGE_END);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
        pack();
    }

    public void updateGame(GameSnapshot gameSnapshot) {
        this.game = gameSnapshot;
        gamePanel.updateGame(gameSnapshot);

        updatePlayerPanels();

    }

    public void showWinner() {
        int p1 = game.getPlayer1Boxes().length;
        int p2 = game.getPlayer2Boxes().length;
        String text;

        if (p1 > p2) {
            text = game.getPlayer1Name() + " wins " + p1 + "–" + p2 + "!";
        } else if (game.getPlayer1Boxes().length < game.getPlayer2Boxes().length) {
            text = game.getPlayer2Name() + " wins " + p2 + "–" + p1 + "!";
        } else {
            text = "It's a tie! " + p1 + "–" + p2;
        }

        winnerLabel.setText(text);
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

        if (gameUpdateType == GameUpdateType.GAME_END) {
            showWinner();
        }
    }
}
