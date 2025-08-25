package dots.foureighty.players.robots.algorithms.mcts.conditional;

import java.util.function.Supplier;

public class TimedBasedConditional implements Supplier<MCTSConditional> {
    private long thinkingTime;

    public TimedBasedConditional(long thinkingTimeMs) throws IllegalArgumentException{
        this.thinkingTime = thinkingTimeMs;
    }
    @Override
    public MCTSConditional get() {
        return new MCTSConditional() {
            private long endTime = System.currentTimeMillis() + thinkingTime;
            @Override
            public boolean runAgain() {
                return System.currentTimeMillis() < endTime;
            }
        };
    }
}
