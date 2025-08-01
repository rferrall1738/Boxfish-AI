package dots.foureighty.players.robots;

public interface Heuristic<T> {
    float evaluate(T input);
}
