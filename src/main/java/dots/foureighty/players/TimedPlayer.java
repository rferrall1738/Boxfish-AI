package dots.foureighty.players;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.packages.ExpirableMovePackage;
import dots.foureighty.lines.packages.MovePackage;
import dots.foureighty.players.robots.RandomBot;

import java.awt.*;

public abstract class TimedPlayer extends Player {
    public TimedPlayer(String name, Color color) {
        super(name, color);
    }

    /***
     * Max mount of time before a move can be made;
     * @return time limit in miliseconds
     */
    protected abstract int getTimeLimit();

    @Override
    public final Move getMove(GameSnapshot gameState) {
        ExpirableMovePackage movePackage = new ExpirableMovePackage();
        movePackage.setMove(new RandomBot().getMove(gameState)); //Populate it with a random move in case anything isn't found in time;
        Thread searchThread = new Thread(() -> {
            synchronized (movePackage) {
                searchForBestMove(gameState, movePackage);
            }
        });
        searchThread.start();
        try {
            Thread.sleep(getTimeLimit());
            searchThread.interrupt();
            movePackage.expire();
            return movePackage.getMove();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * Begin the search for the best move
     * @param gameState
     * @param bestMove
     */
    protected abstract void searchForBestMove(GameSnapshot gameState, MovePackage bestMove);
}
