package dots.foureighty.players.robots;

import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

public abstract class SkippableNeighborGenerator<InputType,TransitionType> extends NeighborGenerator<InputType, TransitionType> {
    @Override
    public abstract SkippableIterator<Pair<InputType, TransitionType>> getNeighbors(InputType input);
}