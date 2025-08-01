package dots.foureighty.players.robots.dumb;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.packages.MovePackage;
import dots.foureighty.players.TimedPlayer;

import java.awt.*;

public class DelayedGreedyBot extends TimedPlayer {
    private final int timeLimit;
    private Color color = Color.ORANGE;

    public DelayedGreedyBot(int timeLimit) {
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


    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return "DelayedGreedyBot";
    }
}
