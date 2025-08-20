package dots.foureighty.gui;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.players.LocalHumanPlayer;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.dumb.RandomBot;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;
import dots.foureighty.players.robots.searchbots.mcts.MCTSBot;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JFrame {

    private JComboBox<String> player1Type;
    private JComboBox<String> player2Type;
    private JSpinner depthSpinner1;
    private JSpinner depthSpinner2;
    private JSpinner maxSpinner1;
    private JSpinner maxSpinner2;
    private JTextField p1NameField;
    private JTextField p2NameField;
    private Color selectedColor1 = Color.RED;
    private Color selectedColor2 = Color.BLUE;
    private JSpinner xSizeSpin;
    private JSpinner ySizeSpin;

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

        // Board size row (min 3x3)
        panel.add(createBoardSizePanel());

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

    // Creates text input for board size, minimum is 3x3
    private JPanel createBoardSizePanel() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row.add(new JLabel("Board size:"));

        xSizeSpin = new JSpinner(new SpinnerNumberModel(5, 3, 30, 1));
        ySizeSpin = new JSpinner(new SpinnerNumberModel(5, 3, 30, 1));

        row.add(new JLabel(" X:"));
        row.add(xSizeSpin);
        row.add(new JLabel(" Y:"));
        row.add(ySizeSpin);

        return row;
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

        JComboBox<String> playerType = new JComboBox<>(new String[]{"Human Player", "Random Bot", "Greedy Bot", "Alpha-Beta Bot", "Monte Carlo Tree Search Bot"});
        panel.add(playerType);

        // Name (for humans)
        JLabel nameLbl = new JLabel("Name:");
        JTextField nameField = new JTextField(isPlayer1 ? "Player 1" : "Player 2", 10);
        panel.add(nameLbl);
        panel.add(nameField);

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
        panel.add(colorPicker);

        // Alpha-Beta depth (numeric)
        JLabel depthLbl = new JLabel("Depth:");
        JSpinner depthSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1)); // default 3
        panel.add(depthLbl);
        panel.add(depthSpinner);

        // MonteCarlo depth (numeric)
        JLabel maxIterationsLbl = new JLabel("Max Iteration:");
        JSpinner maxIterationSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 50000, 1)); // default 1000
        panel.add(maxIterationsLbl);
        panel.add(maxIterationSpinner);

        // Initial visibility
        Runnable toggle = () -> {
            String choice = (String) playerType.getSelectedItem();
            boolean isHuman = "Human Player".equals(choice);
            boolean isAlpha = "Alpha-Beta Bot".equals(choice);
            boolean isMCTS = "Monte Carlo Tree Search Bot".equals(choice);

            nameLbl.setVisible(isHuman);
            nameField.setVisible(isHuman);
            colorPicker.setVisible(isHuman);

            depthLbl.setVisible(isAlpha);
            depthSpinner.setVisible(isAlpha);

            maxIterationsLbl.setVisible(isMCTS);
            maxIterationSpinner.setVisible(isMCTS);

            panel.revalidate();
            panel.repaint();
        };
        playerType.addActionListener(e -> toggle.run());
        toggle.run();

        if (isPlayer1) {
            this.player1Type = playerType;
            this.depthSpinner1 = depthSpinner;
            this.maxSpinner1 = maxIterationSpinner;
            this.p1NameField = nameField;
        } else {
            this.player2Type = playerType;
            this.depthSpinner2 = depthSpinner;
            this.maxSpinner2 = maxIterationSpinner;
            this.p2NameField = nameField;
        }

        return panel;
    }

    private void startGame() {
        getContentPane().removeAll();

        int x = (int) xSizeSpin.getValue();
        int y = (int) ySizeSpin.getValue();

        if (x < 3 || y < 3) {
            JOptionPane.showMessageDialog(this, "Board must be at least 3 × 3.", "Invalid Size", JOptionPane.WARNING_MESSAGE);
            revalidate();
            repaint();
            return;
        }

        Player p1 = makePlayer(
                (String) player1Type.getSelectedItem(),
                validateName(p1NameField.getText(), "Player 1"),
                selectedColor1,
                depthSpinner1,
                maxSpinner1
        );

        Player p2 = makePlayer(
                (String) player2Type.getSelectedItem(),
                validateName(p2NameField.getText(), "Player 2"),
                selectedColor2,
                depthSpinner2,
                maxSpinner2
        );

        new GameFactory()
                .withXSize(x).withYSize(y)
                .withUpdateListener(new GameWatcher())
                .withBoardGenerator(StandardBoards.AMERICAN)
                .withPlayer1(p1)
                .withPlayer2(p2)
                .build()
                .play();

        setVisible(false);
        dispose();
    }

    private String validateName(String s, String fallback) {
        if (s == null) return fallback;
        String t = s.trim();
        return t.isEmpty() ? fallback : t;
    }

    private Player makePlayer(String type, String name, Color color, JSpinner depthSpinner, JSpinner maxIterationSpinner) {
        switch (type) {
            case "Greedy Bot":
                return new GreedyBot();
            case "Alpha-Beta Bot":
                return new AlphaBetaBot((Integer) depthSpinner.getValue());
            case "Monte Carlo Tree Search Bot":
                return new MCTSBot((Integer) maxIterationSpinner.getValue());
            case "Random Bot":
                return new RandomBot();
            case "Human Player":
            default:
                return new LocalHumanPlayer(name, color);
        }
    }
}