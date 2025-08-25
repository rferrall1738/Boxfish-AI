package dots.foureighty.gui.ui.startscreen;


import dots.foureighty.lines.Move;
import dots.foureighty.players.LocalHumanPlayer;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.RobotTypes;
import dots.foureighty.players.robots.TimeableBots;
import dots.foureighty.players.robots.algorithms.mcts.IterationsBasedMCTSSearchAlgorithm;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.dumb.RandomBot;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.searchbots.mcts.IterationsBasedMCTSBot;
import dots.foureighty.players.robots.searchbots.mcts.ParallelMCTSBot;
import dots.foureighty.players.robots.searchbots.mctsmax.MCTSMaxBot;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;
import dots.foureighty.players.robots.searchbots.minimax.MinimaxBot;
import dots.foureighty.players.robots.searchbots.minimax.ParallelMaxBot;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class PlayerSelector extends JPanel {
    private final static HashMap<String, RobotTypes> ROBOT_CONVERSIONS = new HashMap<>();

    private Color selectedColor;
    private JComboBox<RobotTypes> playerTypeSelector;
    private JSpinner depthSpinner;
    private JSpinner iterationsSpinner;
    private JTextField humanNameInput;
    private JComboBox<TimeableBots> timedBotSelector;
    private JSpinner thinkingTimeSpinner;
    private JSpinner switchSpinner;



    public PlayerSelector(boolean isPlayer1, Color backgroundColor) {
        setBackground(backgroundColor);
        setLayout(new FlowLayout());

        // Label for players
        JLabel playerLabel = new JLabel(isPlayer1 ? "Player 1" : "Player 2" + ":");
        add(playerLabel);

        playerTypeSelector = new JComboBox<>(RobotTypes.values());
        add(playerTypeSelector);

        // Name (for humans)
        JLabel humanNameLabel = new JLabel("Name:");
        humanNameInput = new JTextField(isPlayer1 ? "Player 1" : "Player 2", 10);
        add(humanNameLabel);
        add(humanNameInput);

        // Color picker
        JButton colorPicker = new JButton("Pick Color");
        colorPicker.setVisible(true);
        colorPicker.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Choose Color", selectedColor);
            if (chosen != null) {
                selectedColor = chosen;
                colorPicker.setBackground(chosen);
            }
        });
        add(colorPicker);

        // Alpha-Beta depth (numeric)
        JLabel depthLbl = new JLabel("Depth:");
        depthSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1)); // default 3
        add(depthLbl);
        add(depthSpinner);

        // MonteCarlo depth (numeric)
        JLabel maxIterationsLbl = new JLabel("Max Iteration:");
         iterationsSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 50000, 1)); // default 1000
        add(maxIterationsLbl);
        add(iterationsSpinner);

        JLabel switchLbl = new JLabel("Switch Rate:");
         switchSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, .01));
         add(switchLbl);
         add(switchSpinner);

        //Timed bot
        JLabel timedBotLabel = new JLabel("Bot Type:");
        timedBotSelector = new JComboBox<>(TimeableBots.values());
        JLabel thinkingTimeLabel = new JLabel("Thinking Time (ms):");
        thinkingTimeSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 100000, 500));
        add(timedBotLabel);
        add(timedBotSelector);
        add(thinkingTimeLabel);
        add(thinkingTimeSpinner);


        // Initial visibility
        Runnable toggle = () -> {
            RobotTypes type = (RobotTypes) playerTypeSelector.getSelectedItem();

            humanNameLabel.setVisible(type == RobotTypes.HUMAN);
            humanNameInput.setVisible(type == RobotTypes.HUMAN);
            colorPicker.setVisible(type == RobotTypes.HUMAN);

            depthLbl.setVisible(type == RobotTypes.ALPHA_BETA || type == RobotTypes.MINIMAX || type == RobotTypes.MCTS_MAX|| type == RobotTypes.PARALLEL_MINIMAX);
            depthSpinner.setVisible(type == RobotTypes.ALPHA_BETA || type == RobotTypes.MINIMAX || type == RobotTypes.MCTS_MAX || type == RobotTypes.PARALLEL_MINIMAX);

            maxIterationsLbl.setVisible(type == RobotTypes.MCTS|| type == RobotTypes.MCTS_MAX|| type == RobotTypes.PARALLEL_MCTS);
            iterationsSpinner.setVisible(type == RobotTypes.MCTS|| type == RobotTypes.MCTS_MAX|| type == RobotTypes.PARALLEL_MCTS);

            timedBotSelector.setVisible(type == RobotTypes.TIMED);
            thinkingTimeSpinner.setVisible(type == RobotTypes.TIMED);
            thinkingTimeLabel.setVisible(type == RobotTypes.TIMED);
            timedBotLabel.setVisible(type == RobotTypes.TIMED);

            switchLbl.setVisible(type == RobotTypes.MCTS_MAX);
            switchSpinner.setVisible(type == RobotTypes.MCTS_MAX);


            revalidate();
            repaint();
        };
        playerTypeSelector.addActionListener(e -> toggle.run());
        toggle.run();

    }


    public Player makePlayer() {
        switch ((RobotTypes) playerTypeSelector.getSelectedItem()) {
            case MCTS:
                return new IterationsBasedMCTSBot((int) iterationsSpinner.getValue());
            case ALPHA_BETA:
                return new AlphaBetaBot((int) depthSpinner.getValue());
            case GREEDY:
                return new GreedyBot();
            case RANDOM:
                return new RandomBot();
            case MINIMAX:
                return new MinimaxBot((int) iterationsSpinner.getValue());
            case PARALLEL_MCTS:
                return new ParallelMCTSBot((int) iterationsSpinner.getValue());
            case PARALLEL_MINIMAX:
                return new ParallelMaxBot((int) depthSpinner.getValue());
            case MCTS_MAX:
                int iterations = (int) iterationsSpinner.getValue();
                int depth = (int) depthSpinner.getValue();
                double switchRate = (double) switchSpinner.getValue();
                IterationsBasedMCTSSearchAlgorithm<DABState, Move> mcts = new IterationsBasedMCTSSearchAlgorithm<>(iterations);
                return new MCTSMaxBot(mcts, depth, switchRate);
            case TIMED:
                return ((TimeableBots) timedBotSelector.getSelectedItem()).getBot((Integer) thinkingTimeSpinner.getValue());
            case HUMAN:
            default:
                    return new LocalHumanPlayer(humanNameInput.getText(), selectedColor);
        }
    }

}
