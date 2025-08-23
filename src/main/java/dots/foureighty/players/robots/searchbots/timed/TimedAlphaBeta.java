package dots.foureighty.players.robots.searchbots.timed;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.packages.ExpirableMovePackage;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.dumb.RandomBot;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;

import java.awt.*;
import java.util.HashMap;

public class TimedAlphaBeta implements TimedBot {

    private final HashMap<Integer,AlphaBetaBot> depthBasedBots = new HashMap<>();
    private final long thinkingTime;
    private final Heuristic<DABState>[] heuristics;
    private Color color = Color.ORANGE;

    /***
     * Creates a timedbot that only has the given amount of time to think about the next move
     * @param thinkingTimeMS How long the bot can think (in ms)
     * @param heuristics The heuristics to use
     */
    public TimedAlphaBeta(long thinkingTimeMS, Heuristic<DABState>... heuristics) {
        this.thinkingTime = thinkingTimeMS;
        this.heuristics = heuristics;
    }

    private AlphaBetaBot getBot(int depth) {
            return depthBasedBots.computeIfAbsent(depth, (d) -> new AlphaBetaBot(d,heuristics));
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return "Thinking Bot (AlphaBeta) " + thinkingTime + "ms";
    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        ExpirableMovePackage movePackage = new ExpirableMovePackage();
        movePackage.setMove(new RandomBot().getMove(gameState));

        Thread searchThread = new Thread(() -> {
            synchronized (movePackage) {
                int depth = 1;
                while (!movePackage.isExpired()) {
                    AlphaBetaBot bot = getBot(depth);
                    try {
                        movePackage.setMove(bot.getMove(gameState));
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted");
                    }
                   depth++;
                }
            }
        });

        searchThread.start();
        try {
            Thread.sleep(this.getThinkingTime());
            movePackage.expire();
            searchThread.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return movePackage.getMove();
    }

    @Override
    public long getThinkingTime() {
        return thinkingTime;
    }
}
