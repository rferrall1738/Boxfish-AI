package algorithms.minimax;

import dots.foureighty.players.robots.SkippableNeighborGenerator;
import dots.foureighty.players.robots.algorithms.Evaluator;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import dots.foureighty.players.robots.algorithms.mcts.IterationsBasedMCTSSearchAlgorithm;
import dots.foureighty.players.robots.algorithms.mctsmax.MCTSMaxAlgorithm;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

import java.util.LinkedList;


public class BinaryTreeMCTSMaxSearch extends MCTSMaxAlgorithm<BinaryTreeNode, TreeSide> {

    private final SkippableNeighborGenerator<BinaryTreeNode, TreeSide> neighborGenerator =
            new SkippableNeighborGenerator<BinaryTreeNode, TreeSide>() {
                @Override
                public SkippableIterator<Pair<BinaryTreeNode, TreeSide>> getNeighbors(BinaryTreeNode input) {
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

    public BinaryTreeMCTSMaxSearch(IterationsBasedMCTSSearchAlgorithm<BinaryTreeNode, TreeSide> mcts, int abDepth, double switchRate) {
        super(mcts, abDepth, switchRate);
    }


    public Pair<LinkedList<TreeSide>, Float> search(BinaryTreeNode input) throws InterruptedException {
        reset();
        return super.search(input, neighborGenerator, evaluator, -1, true);
    }
}