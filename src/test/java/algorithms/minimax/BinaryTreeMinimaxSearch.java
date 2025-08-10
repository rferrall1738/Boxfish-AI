package algorithms.minimax;

import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import dots.foureighty.players.robots.algorithms.minimax.MinimaxSearchAlgorithm;
import dots.foureighty.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;

public class BinaryTreeMinimaxSearch extends MinimaxSearchAlgorithm<BinaryTreeNode, TreeSide> {

    NeighborGenerator generator = new NeighborGenerator() {
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
    Evaluator evaluator = new Evaluator() {
        @Override
        public float evaluate(BinaryTreeNode input) {
            if (input instanceof BinaryTreeValue) {
                BinaryTreeValue value = (BinaryTreeValue) input;
                return value.getValue();
            }
            return Integer.MIN_VALUE;
        }
    };

    public Pair<LinkedList<TreeSide>, Float> search(BinaryTreeNode input) {
        return super.search(input, generator, evaluator, -1, true);
    }
}
