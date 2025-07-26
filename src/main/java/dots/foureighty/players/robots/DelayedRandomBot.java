package dots.foureighty.players.robots;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.packages.MovePackage;
import dots.foureighty.players.TimedPlayer;

import java.awt.*;

/***
 * Randombot but it waits 0.5 seconds before moving
 */
public class DelayedRandomBot extends TimedPlayer {
    public DelayedRandomBot() {
        super("Delayed Random Bot", Color.ORANGE);
    }


    @Override
    protected int getTimeLimit() {
        return 500;
    }

    private final RandomBot randomBot = new RandomBot();

    @Override
    protected void searchForBestMove(GameSnapshot gameState, MovePackage bestMove) {
        bestMove.setMove(randomBot.getMove(gameState));
    }
}
