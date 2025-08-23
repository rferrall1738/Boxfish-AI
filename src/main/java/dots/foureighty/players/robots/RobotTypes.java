package dots.foureighty.players.robots;

public enum RobotTypes {
    HUMAN("Human"),
    GREEDY("Greedy Bot"),
    RANDOM( "Random Bot"),
    ALPHA_BETA( "Alpha Beta Bot"),
    MCTS("Monte Carlo Tree Search Bot"),
    MINIMAX("Mini Max Bot"),
    TIMED("Timed Bot"),
    PARALLEL_MCTS("Parallel MCTS Bot"),
    PARALLEL_MINIMAX("Parallel Mini Max Bot"),
    MCTS_MAX("MCTS Max Bot");

    private final String name;

    RobotTypes(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}