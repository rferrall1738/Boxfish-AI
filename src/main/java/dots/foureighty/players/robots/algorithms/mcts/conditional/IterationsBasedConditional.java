package dots.foureighty.players.robots.algorithms.mcts.conditional;

import java.util.function.Supplier;

public class IterationsBasedConditional implements Supplier<MCTSConditional> {
    private int iterations;
    public IterationsBasedConditional(int maxIterations) throws IllegalArgumentException{
        this.iterations = maxIterations;
    }
    @Override
    public MCTSConditional get() {
        return new MCTSConditional() {
            private int start = 0;
            @Override
            public boolean runAgain() {
                return start++ < iterations;
            }
        };
    }
}
