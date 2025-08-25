package dots.foureighty.players.robots;

import dots.foureighty.players.Player;
import dots.foureighty.players.robots.searchbots.timed.TimedAlphaBetaBot;
import dots.foureighty.players.robots.searchbots.timed.TimedBot;
import dots.foureighty.players.robots.searchbots.timed.TimedMCTSBot;

public enum TimeableBots {
    ALPHA_BETA(TimedAlphaBetaBot::new, RobotTypes.ALPHA_BETA),
    MCTS(TimedMCTSBot::new, RobotTypes.MCTS);

    private final ThinkingSupplier botSupplier;
    private final RobotTypes type;

    TimeableBots(ThinkingSupplier botSupplier, RobotTypes type) {
        this.botSupplier = botSupplier;
        this.type = type;
    }

    interface ThinkingSupplier<T extends TimedBot> {
        T getBot(long thinkingTimeMS);
    }

    public Player getBot(long thinkingTimeMS) {
        return botSupplier.getBot(thinkingTimeMS);
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
