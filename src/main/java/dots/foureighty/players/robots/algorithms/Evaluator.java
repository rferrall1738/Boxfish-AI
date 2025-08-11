package dots.foureighty.players.robots.algorithms;

public abstract class Evaluator<NodeType> {
    /***
     * When given a final state, evaluates how good the output is
     * @param input Final state to evaluate.
     * @return Metric to determine how good the output is.
     */
    public abstract float evaluate(NodeType input);
}