package dots.foureighty.players.robots.algorithms.mcts;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.players.robots.algorithms.SearchAlgorithm;
import dots.foureighty.util.Pair;

import java.util.*;

public abstract class MCTSSearchAlgorithm<NodeType, TransitionType> extends SearchAlgorithm<NodeType, TransitionType> {

    private final int maxIterations;
    private final double explorationParameter;
    private final static Random RANDOM = new Random();
    private final static int MAX_SIMULATION_DEPTH = 100; //MAX size of tree

    /***
     * Monte Carlo Tree Search
     * @param maxIterations number of iterations to run MCTS
     * @param explorationParameter Exploration coefficient. Must be >= 0.
     *                             Higher values prioritize exploration of new nodes.
     * @throws IllegalArgumentException explorationParameter is < 0
     */
    public MCTSSearchAlgorithm(int maxIterations, double explorationParameter) throws IllegalArgumentException{
        this.maxIterations = maxIterations;
        this.explorationParameter = explorationParameter;
        if (explorationParameter < 0) {
            throw new IllegalArgumentException("explorationParameter must be >= 0");
        }
    }

    /***
     * Monte Carlo Tree Search
     * exploration constant in sqrt(2)
     * @param maxIterations number of MCTS iterations
     */
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

    /***
     * Searches a monte carlo tree for the terminal state with the highest UCB1
     * @param node Tree node to search
     * @return MCTS Leaf
     */
    private MCTSNode select(MCTSNode node) {
        while(!node.isTerminal() && node.isFullyExpanded()){
            node = node.getBestUCB1Child();
            if(node == null){
                break;
            }
        }
        return node;
    }

    /***
     * Returns a child MCTSNode that has not yet been explored.
     * @param node parentNode to generate children from
     * @param neighborGenerator Neighbor Generator for NodeType
     * @return Generated node
     */
    private MCTSNode expand(MCTSNode node, NeighborGenerator<NodeType, TransitionType> neighborGenerator){
        if(node.isTerminal() || !node.hasUntried()){
            return null;
        }
        int randomIndex = RANDOM.nextInt(node.getUntriedMoves().size());
        Pair<NodeType, TransitionType> chosenMove = node.getUntriedMoves().remove(randomIndex);
        MCTSNode newChild = new MCTSNode(chosenMove.getKey(), chosenMove.getValue(), node, neighborGenerator);
        node.getChildren().add(newChild);

        return newChild;
    }

    /***
     * Runs a simulated MCTS on a node.
     * @param node Node to run MCTS on.
     * @param neighborGenerator Function that generates child nodes
     * @param evaluator Function that evaluates end states
     * @return computer best value.
     */
    private double simulate(MCTSNode node, NeighborGenerator<NodeType, TransitionType> neighborGenerator,
                            Evaluator<NodeType> evaluator) {
        NodeType currentState = node.getState();
        int simulationDepth = 0;
        while(simulationDepth < MAX_SIMULATION_DEPTH){
            Iterator<Pair<NodeType, TransitionType>> neighbors = neighborGenerator.getNeighbors(currentState);
            List<Pair<NodeType, TransitionType>> moveList = new ArrayList<>();
            while(neighbors.hasNext()){
                moveList.add(neighbors.next());
            }
            if (moveList.isEmpty()){
                break; // reached terminal state
            }
            Pair<NodeType, TransitionType> randomType = moveList.get(RANDOM.nextInt(moveList.size()));
            currentState =randomType.getKey();
            simulationDepth++;
        }
        return evaluator.evaluate(currentState);
    }

    /***
     * Backpropagates a reward up a search tree
     * @param node terminal node
     * @param reward reward at terminal node
     */
    private void backpropagate(MCTSNode node, double reward){
        if (node == null) {
            return;
        }
        node.visitNode();
        node.addReward(reward);
        backpropagate(node.getParent(), reward);
    }

    /***
     * Helper class for storing data of MCTS node states
     */
    private class MCTSNode {
        private final NodeType state;
        private final TransitionType moveFromParent;
        private final MCTSNode parent;
        private final LinkedList<MCTSNode> children;

        private int visitCount;
        private double totalReward;
        private final ArrayList<Pair<NodeType, TransitionType>> untriedMoves;

        public MCTSNode(NodeType state, TransitionType moveFromParent, MCTSNode parent,
                        NeighborGenerator<NodeType, TransitionType> neighborGenerator) {
            this.state = state;
            this.moveFromParent = moveFromParent;
            this.parent = parent;
            this.children = new LinkedList<>();
            this.visitCount = 0;
            this.totalReward = 0.0;
            this.untriedMoves = generateUntriedMoves(state, neighborGenerator);
        }

        private ArrayList<Pair<NodeType, TransitionType>> generateUntriedMoves(NodeType state, NeighborGenerator<NodeType,
                TransitionType> neighborGenerator) {
            ArrayList<Pair<NodeType, TransitionType>> moves = new ArrayList<>();
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

        public ArrayList<Pair<NodeType, TransitionType>> getUntriedMoves() {
            return untriedMoves;
        }

        /***
         * Gets a list of children
         * @return child nodes
         */
        public LinkedList<MCTSNode> getChildren() {
            return children;
        }

        /***
         * Gets the parent node of a MCTS Node
         * @return the parent node
         */
        public MCTSNode getParent() {
            return parent;
        }

        /***
         * Gets the unwrapped node
         * @return The state MCTS is evaluating
         * */
        public NodeType getState() {
            return state;
        }

        /***
         * Computes the average reward
         * @return totalReward / visitCount
         */
        public double getAverageReward() {
            if (visitCount == 0) {
                return 0.0;
            } else {
                return totalReward / visitCount;
            }
        }

        /***
         * Increases the visit count of a node
         */
        public void visitNode() {
            visitCount++;
        }

        /***
         * Adds a reward to a node
         * @param reward amount of award to add to the node
         */
        public void addReward(double reward) {
            totalReward += reward;
        }

        /***
         * Computes UCB1 value of a node
         * @return computed UCB1
         */
        public double getUCB1value() {
            if (visitCount == 0) {
                return Double.MAX_VALUE;
            }
            double exploitation = getAverageReward();
            double exploration = explorationParameter * Math.sqrt(Math.log(parent.visitCount) / visitCount);
            return exploitation + exploration;
        }

        /***
         * Searches through node children and finds the child with the best UCB1
         * @return child node with highest UCB1
         */
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

        /***
         * Finds the child that has been visited the most
         * @return the most visited child node
         */
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
}
