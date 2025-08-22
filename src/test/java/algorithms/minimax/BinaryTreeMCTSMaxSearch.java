package algorithms.minimax;

import dots.foureighty.players.robots.SkippableNeighborGenerator;
import dots.foureighty.players.robots.algorithms.Evaluator;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.TreeSide;
import dots.foureighty.players.robots.algorithms.mcts.MCTSSearchAlgorithm;
import dots.foureighty.players.robots.algorithms.mctsmax.MCTSMaxAlgorithm;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

import java.util.LinkedList;


public class BinaryTreeMCTSMaxSearch extends MCTSMaxAlgorithm<BinaryTreeNode, TreeSide> {

    private final SkippableNeighborGenerator<BinaryTreeNode, TreeSide> neighborGenerator =
            new SkippableNeighborGenerator<BinaryTreeNode, TreeSide>() {
                @Override
                public SkippableIterator<Pair<BinaryTreeNode, TreeSide>> getNeighbors(BinaryTreeNode input) {
                    LinkedList<Pair<BinaryTreeNode, TreeSide>> neighbors = new LinkedList<>();
                    if (input instanceof BinaryTreeBranch) {
                        BinaryTreeBranch branch = (BinaryTreeBranch) input;
                        neighbors.add(new Pair<>(branch.getLeft(), TreeSide.LEFT));
                        neighbors.add(new Pair<>(branch.getRight(), TreeSide.RIGHT));
                    }
                    return new SkippableTreeIterator(input);
                }
            };

    private final Evaluator<BinaryTreeNode> evaluator = new Evaluator<BinaryTreeNode>() {
        @Override
        public float evaluate(BinaryTreeNode input) {
            if (input instanceof BinaryTreeValue) {
                return ((BinaryTreeValue) input).getValue();
            }

            return Integer.MIN_VALUE;
        }
    };

    public BinaryTreeMCTSMaxSearch(MCTSSearchAlgorithm<BinaryTreeNode, TreeSide> mcts, int abDepth, double switchRate) {
        super(mcts, abDepth, switchRate);
    }


    public Pair<LinkedList<TreeSide>, Float> search(BinaryTreeNode input) {
        reset();
        return super.search(input, neighborGenerator, evaluator, -1, true);
    }
}