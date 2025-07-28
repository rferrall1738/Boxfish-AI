package dots.foureighty.players.robots;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.packages.MovePackage;
import dots.foureighty.players.TimedPlayer;

import java.awt.*;

public class DelayedGreedyBot extends TimedPlayer {
    private final int timeLimit;

    public DelayedGreedyBot(int timeLimit) {
        super("Delayed Greedy Bot", Color.ORANGE);
        this.timeLimit = timeLimit;
    }

    @Override
    protected int getTimeLimit() {
        return timeLimit;
    }

    @Override
    protected void searchForBestMove(GameSnapshot gameState, MovePackage bestMove) {
        GreedyBot greedyBot = new GreedyBot();
        bestMove.setMove(greedyBot.getMove(gameState));
    }
}
