package dots.foureighty.players.robots.searchbots.timed;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.packages.ExpirableMovePackage;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.dumb.RandomBot;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.searchbots.SearchBot;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class TimedMinimaxBot<T extends SearchBot> implements Player {

    private final Constructor<T> constructor;
    private final HashMap<Integer,T> depthBasedBots = new HashMap<>();
    private final long thinkingTime;
    private final Heuristic<DABState>[] heuristics;
    private Color color = Color.ORANGE;

    /***
     * Creates a timedbot that only has the given amount of time to think about the next move
     * @param tClass Class of the bot to use
     * @param thinkingTimeMS How long the bot can think (in ms)
     * @param heuristics The heuristics to use
     */
    public TimedMinimaxBot(Class<T> tClass, long thinkingTimeMS, Heuristic<DABState>... heuristics) {
        this.thinkingTime = thinkingTimeMS;

        this.heuristics = heuristics;
        try {
            constructor = tClass.getConstructor(int.class, Heuristic[].class);
        } catch (NoSuchMethodException e) {
            throw new  RuntimeException("Provided MinimaxBot does not have a default constructor",e);
        }
    }

    private T getBot(int depth) {
            return depthBasedBots.computeIfAbsent(depth, (d) -> {
                try {
                    return constructor.newInstance(d,heuristics);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
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
        return "Thinking Bot (" + constructor.getName() + ") " + thinkingTime + "ms";
    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        ExpirableMovePackage movePackage = new ExpirableMovePackage();
        movePackage.setMove(new RandomBot().getMove(gameState));

        Thread searchThread = new Thread(() -> {
            synchronized (movePackage) {
                int depth = 1;
                while (!movePackage.isExpired()) {
                    T bot = getBot(depth);
                   movePackage.setMove(bot.getMove(gameState));
                   depth++;
                    System.out.println(depth);
                }
            }
        });

        searchThread.start();
        try {
            Thread.sleep(this.thinkingTime);
            movePackage.expire();
            searchThread.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return movePackage.getMove();
    }
}
