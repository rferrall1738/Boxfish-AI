package algorithms.minimax;

import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import dots.foureighty.players.robots.algorithms.AlphaBetaSearchAlgorithm;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

import java.util.LinkedList;

public class BinaryTreeAlphaBetaSearch extends AlphaBetaSearchAlgorithm<BinaryTreeNode, TreeSide> {

    private final SkippableNeighborGenerator neighborGenerator = new SkippableNeighborGenerator() {
        @Override
        protected SkippableIterator<Pair<BinaryTreeNode, TreeSide>> getNeighbors(BinaryTreeNode input) {
            return new SkippableTreeIterator(input);
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
        return super.search(input, neighborGenerator, evaluator, -1, true);
    }
}
