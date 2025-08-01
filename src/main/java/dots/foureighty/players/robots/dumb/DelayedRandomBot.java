package dots.foureighty.players.robots.dumb;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.packages.MovePackage;
import dots.foureighty.players.TimedPlayer;

import java.awt.*;

/***
 * Randombot but it waits 0.5 seconds before moving
 */
public class DelayedRandomBot extends TimedPlayer {
    private final int delay;
    private final RandomBot randomBot = new RandomBot();
    private Color color = Color.PINK;


    public DelayedRandomBot(int delay) {
        this.delay = delay;
    }


    @Override
    protected int getTimeLimit() {
        return delay;
    }


    @Override
    protected void searchForBestMove(GameSnapshot gameState, MovePackage bestMove) {
        bestMove.setMove(randomBot.getMove(gameState));
    }

    /**
     * @return
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * @param color
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return "DelayedRandomBot";
    }
}
