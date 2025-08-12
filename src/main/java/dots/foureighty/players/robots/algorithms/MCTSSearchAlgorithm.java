package dots.foureighty.players.robots.algorithms;
import dots.foureighty.util.Pair;

import java.util.*;

public class MCTSSearchAlgorithm<NodeType, TransitionType> extends SearchAlgorithm<NodeType, TransitionType> {

    private final int maxIterations;
    private final double explorationParameter;
    private final Random random;

    public MCTSSearchAlgorithm(int maxIterations, double explorationParameter) {
        this.maxIterations = maxIterations;
        this.explorationParameter = explorationParameter;
        this.random = new Random();
    }

    public MCTSSearchAlgorithm(int maxIterations) {
        this(maxIterations, Math.sqrt(2.0));
    }

    @Override
    protected Pair<LinkedList<TransitionType>, Float> search(NodeType input, NeighborGenerator<NodeType, TransitionType>
            neighborGenerator, Evaluator<NodeType> evaluator) {
        MCTSNode root = new MCTSNode(input, null, null, neighborGenerator);
        for(int i = 0; i< maxIterations;i++){
            MCTSNode selectedNode =select(root);
            MCTSNode expandedNode = expand(selectedNode, neighborGenerator);
            if(expandedNode != null){
                double reward = simulate(expandedNode, neighborGenerator, evaluator);
                backpropagate(expandedNode, reward);
            }
            else{
                double reward = simulate(selectedNode, neighborGenerator, evaluator);
                backpropagate(selectedNode, reward);
            }

        }
        MCTSNode bestChild = root.getMostVisitedChild();
        if(bestChild == null){
            return new Pair<>(new LinkedList<>(), evaluator.evaluate(input));
        }
        LinkedList<TransitionType> moveSequence = new LinkedList<>();
        moveSequence.add(bestChild.moveFromParent);

        return new Pair<>(moveSequence, (float) bestChild.getAverageReward());
    }

    private class MCTSNode {
        NodeType state;
        TransitionType moveFromParent;
        MCTSNode parent;
        List<MCTSNode> children;

        int visitCount;
        double totalReward;
        List<Pair<NodeType, TransitionType>> untriedMoves;

        public MCTSNode(NodeType state, TransitionType moveFromParent, MCTSNode parent,
                        NeighborGenerator<NodeType, TransitionType> neighborGenerator) {
            this.state = state;
            this.moveFromParent = moveFromParent;
            this.parent = parent;
            this.children = new ArrayList<>();
            this.visitCount = 0;
            this.totalReward = 0.0;
            this.untriedMoves = generateUntriedMoves(state, neighborGenerator);
        }

        private List<Pair<NodeType, TransitionType>> generateUntriedMoves(NodeType state, NeighborGenerator<NodeType,
                TransitionType> neighborGenerator) {
            List<Pair<NodeType, TransitionType>> moves = new ArrayList<>();
            Iterator<Pair<NodeType, TransitionType>> neighbors = neighborGenerator.getNeighbors(state);
            while (neighbors.hasNext()) {
                moves.add(neighbors.next());
            }
            return moves;
        }

        public boolean isFullyExpanded() {
            return untriedMoves.isEmpty();
        }

        public boolean isTerminal() {
            return untriedMoves.isEmpty() && children.isEmpty();
        }

        public boolean hasUntried() {
            return !untriedMoves.isEmpty();
        }

        public double getAverageReward() {
            if (visitCount == 0) {
                return 0.0;
            } else {
                return totalReward / visitCount;
            }
        }

        public double getUCB1value() {
            if (visitCount == 0) {
                return Double.MAX_VALUE;
            }
            double exploitation = getAverageReward();
            double exploration = explorationParameter * Math.sqrt(Math.log(parent.visitCount) / visitCount);
            return exploitation + exploration;
        }

        public MCTSNode getBestUCB1Child() {
            if (children.isEmpty()) {
                return null;
            }
            MCTSNode bestChild = children.get(0);
            double bestUCB1 = bestChild.getUCB1value();

            for (MCTSNode child : children) {
                double childUCB1 = child.getUCB1value();
                if (childUCB1 > bestUCB1) {
                    bestChild = child;
                    bestUCB1 = childUCB1;
                }
            }
            return bestChild;
        }

        public MCTSNode getMostVisitedChild() {
            if (children.isEmpty()) {
                return null;
            }
            MCTSNode mostVisited = children.get(0);
            for (MCTSNode child : children) {
                if (child.visitCount > mostVisited.visitCount) {
                    mostVisited = child;
                }
            }
            return mostVisited;
        }
    }

    private MCTSNode select(MCTSNode node) {
        while(!node.isTerminal() && node.isFullyExpanded()){
            node = node.getBestUCB1Child();
            if(node == null){
                break;
            }
        }
        return node;
    }

    private MCTSNode expand(MCTSNode node, NeighborGenerator<NodeType, TransitionType> neighborGenerator){
        if(node.isTerminal() || !node.hasUntried()){
            return null;
        }
        int randomIndex = random.nextInt(node.untriedMoves.size());
        Pair<NodeType, TransitionType> chosenMove = node.untriedMoves.remove(randomIndex);
        MCTSNode newChild = new MCTSNode(chosenMove.getKey(), chosenMove.getValue(), node, neighborGenerator);
        node.children.add(newChild);

        return newChild;
    }

    private double simulate(MCTSNode node, NeighborGenerator<NodeType, TransitionType> neighborGenerator,
                            Evaluator<NodeType> evaluator) {
        NodeType currentState = node.state;
        int simulationDepth = 0;
        int maxSimulationDepth = 100;
        while(simulationDepth < maxSimulationDepth){
            Iterator<Pair<NodeType, TransitionType>> neighbors = neighborGenerator.getNeighbors(currentState);
            List<Pair<NodeType, TransitionType>> moveList = new ArrayList<>();
            while(neighbors.hasNext()){
                moveList.add(neighbors.next());
            }
            if (moveList.isEmpty()){
                break; // reached terminal state
            }
            Pair<NodeType, TransitionType> randomType = moveList.get(random.nextInt(moveList.size()));
            currentState =randomType.getKey();
            simulationDepth++;
        }
        return evaluator.evaluate(currentState);
    }

    private void backpropagate(MCTSNode node, double reward){
        while(node != null){
            node.visitCount++;
            node.totalReward += reward;
            node = node.parent;
        }
    }

}
