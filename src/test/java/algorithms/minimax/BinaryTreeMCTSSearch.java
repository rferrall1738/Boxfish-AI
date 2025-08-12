package algorithms.minimax;

import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import dots.foureighty.players.robots.algorithms.Evaluator;
import dots.foureighty.players.robots.algorithms.MCTSSearchAlgorithm;
import dots.foureighty.players.robots.algorithms.NeighborGenerator;
import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;


public class BinaryTreeMCTSSearch extends MCTSSearchAlgorithm<BinaryTreeNode, TreeSide> {

    private final NeighborGenerator<BinaryTreeNode, TreeSide> neighborGenerator =
            new NeighborGenerator<BinaryTreeNode, TreeSide>() {
                @Override
                public Iterator<Pair<BinaryTreeNode, TreeSide>> getNeighbors(BinaryTreeNode input) {
                    LinkedList<Pair<BinaryTreeNode, TreeSide>> neighbors = new LinkedList<>();
                    if (input instanceof BinaryTreeBranch) {
                        BinaryTreeBranch branch = (BinaryTreeBranch) input;
                        neighbors.add(new Pair<>(branch.getLeft(), TreeSide.LEFT));
                        neighbors.add(new Pair<>(branch.getRight(), TreeSide.RIGHT));
                    }
                    return neighbors.iterator();
                }
            };

    private final Evaluator<BinaryTreeNode> evaluator = new Evaluator<BinaryTreeNode>() {
        @Override
        public float evaluate(BinaryTreeNode input) {
            if (input instanceof BinaryTreeValue) {
                BinaryTreeValue value = (BinaryTreeValue) input;
                return value.getValue();
            }
            return Integer.MIN_VALUE;
        }
    };

    public BinaryTreeMCTSSearch(int iterations) {
        super(iterations);
    }


    public Pair<LinkedList<TreeSide>, Float> search(BinaryTreeNode input) {
        return super.search(input, neighborGenerator, evaluator);
    }
}