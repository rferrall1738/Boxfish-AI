package dots.foureighty.gui.ui.startscreen;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.players.LocalHumanPlayer;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.RobotTypes;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.dumb.RandomBot;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;
import dots.foureighty.players.robots.searchbots.mcts.MCTSBot;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class StartScreen extends JFrame {

    private Color selectedColor1 = Color.RED;
    private Color selectedColor2 = Color.decode("0x075aae");
    private Color selectedColor3 = Color.decode("0x97d4b5");
    private Color backgroundColor = Color.decode("0xf9f3de");
    private JSpinner xSizeSpin;
    private JSpinner ySizeSpin;

    private final PlayerSelector player1;
    private final PlayerSelector player2;


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
        panel.setBackground(backgroundColor);
        // Title label
        JLabel title = new JLabel("Welcome to Dots & Boxes!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(selectedColor2);
        panel.add(title);

        // Board size row (min 3x3)
        panel.add(createBoardSizePanel());

        // Player selection area
        player1 = new PlayerSelector(true,backgroundColor);
        player2 = new PlayerSelector(false,backgroundColor);

        panel.add(player1);
        panel.add(player2);

        // Start button to begin game
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.addActionListener(e -> startGame());
        startButton.setBackground(selectedColor3);
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

        row.setBackground(backgroundColor);

        return row;
    }


    private void startGame() {
        getContentPane().removeAll();

        int x = (int) xSizeSpin.getValue();
        int y = (int) ySizeSpin.getValue();

        if (x < 3 || y < 3) {
            JOptionPane.showMessageDialog(this,
                    "Board must be at least 3 × 3.", "Invalid Size", JOptionPane.WARNING_MESSAGE);
            revalidate();
            repaint();
            return;
        }


        new GameFactory()
                .withXSize(x).withYSize(y)
                .withUpdateListener(new GameWatcher())
                .withBoardGenerator(StandardBoards.AMERICAN)
                .withPlayer1(player1.makePlayer())
                .withPlayer2(player2.makePlayer())
                .build()
                .play();

        setVisible(false);
        dispose();
    }

}