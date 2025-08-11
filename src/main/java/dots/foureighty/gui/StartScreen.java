package dots.foureighty.gui;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.players.LocalHumanPlayer;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.dumb.RandomBot;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JFrame {

    private JComboBox<String> player1Type;
    private JComboBox<String> player2Type;
    private JComboBox<Integer> depthPicker1;
    private JComboBox<Integer> depthPicker2;

    private Color selectedColor1 = Color.RED;
    private Color selectedColor2 = Color.BLUE;


    /***
     * Creates a start screen for users to choose bot type for each player
     */
    public StartScreen() {
        setTitle("Dots & Boxes");
        setSize(600, 600);
        // You can close the start window, but the game will still continue
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main container
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title label
        JLabel title = new JLabel("Welcome to Dots & Boxes!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(title);

        // Player selection area
        panel.add(createPlayerSelectionPanel("Player 1", true));
        panel.add(createPlayerSelectionPanel("Player 2", false));

        // Start button to begin game
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.addActionListener(e -> startGame());
        panel.add(startButton);

        // Display the UI
        setContentPane(panel);
        setVisible(true);
    }

    // Creates a row of components to select a player's type, color, and bot depth
    // Needs to be updated with all possible bot types
    // Options right now: random, greedy bot, alpha beta
    private JPanel createPlayerSelectionPanel(String label, boolean isPlayer1) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Label for players
        JLabel playerLabel = new JLabel(label + ":");
        panel.add(playerLabel);

        JComboBox<String> playerType = new JComboBox<>(new String[]{"Human Player", "Random Bot", "Greedy Bot", "Alpha-Beta Bot"});
        panel.add(playerType);

        // Color picker
        JButton colorPicker = new JButton("Pick Color");
        colorPicker.setVisible(true);
        colorPicker.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Choose Color", isPlayer1 ? selectedColor1 : selectedColor2);
            if (chosen != null) {
                if (isPlayer1) selectedColor1 = chosen;
                else selectedColor2 = chosen;
                colorPicker.setBackground(chosen);
            }
        });

        JComboBox<Integer> depthSelector = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        depthSelector.setVisible(false);

        playerType.addActionListener(e -> {
            String choice = (String) playerType.getSelectedItem();
            boolean isHuman = choice.equals("Human Player");
            boolean isAlpha = choice.equals("Alpha-Beta Bot");
            colorPicker.setVisible(isHuman);
            depthSelector.setVisible(isAlpha);
            panel.revalidate();
            panel.repaint();
        });

        panel.add(colorPicker);
        panel.add(depthSelector);

        if (isPlayer1) {
            this.player1Type = playerType;
            this.depthPicker1 = depthSelector;
        } else {
            this.player2Type = playerType;
            this.depthPicker2 = depthSelector;
        }

        return panel;
    }

    private void startGame() {
        getContentPane().removeAll();

        Player p1 = makePlayer((String) player1Type.getSelectedItem(), "Player 1", selectedColor1, depthPicker1);
        Player p2 = makePlayer((String) player2Type.getSelectedItem(), "Player 2", selectedColor2, depthPicker2);

        new GameFactory().withXSize(5).withYSize(5)
                .withUpdateListener(new GameWatcher())
                .withBoardGenerator(StandardBoards.AMERICAN)
                .withPlayer1(p1)
                .withPlayer2(p2)
                .build().play();

        revalidate();
        repaint();
    }

    private Player makePlayer(String type, String name, Color color, JComboBox<Integer> depthPicker) {
        switch (type) {
            case "Greedy Bot":
                return new GreedyBot();
            case "Alpha-Beta Bot":
                return new AlphaBetaBot((Integer) depthPicker.getSelectedItem());
            case "Random Bot":
                return new RandomBot();
            case "Local Human":
            default:
                return new LocalHumanPlayer(name, color);
        }
    }
}
