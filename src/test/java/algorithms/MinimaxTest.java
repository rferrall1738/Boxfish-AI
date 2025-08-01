package algorithms;

import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import algorithms.minimax.BinaryTreeMinimaxSearch;
import dots.foureighty.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;

public class MinimaxTest {

    private static final BinaryTreeNode SINGLE_NODE = new BinaryTreeValue(1);
    private static final BinaryTreeNode SINGLE_BRANCH = new BinaryTreeBranch(1, 2);
    private static final BinaryTreeNode BIG_TREE =
            new BinaryTreeBranch(new BinaryTreeBranch(new BinaryTreeBranch(8, 2), 3), new BinaryTreeBranch(9, -1));

    private static final BinaryTreeMinimaxSearch MINIMAX_SEARCH = new BinaryTreeMinimaxSearch();

    public void runTests() {
        singleNodeSearch();
        singleBranchSearch();
        treeSearch();
    }

    private void singleNodeSearch() {
        assert MINIMAX_SEARCH.search(SINGLE_NODE).equals(new Pair<>(new LinkedList(), 1.0f));
        assert !MINIMAX_SEARCH.search(SINGLE_NODE).equals(new Pair<>(new LinkedList(), 2.0f));
    }

    private void singleBranchSearch() {
        Pair<LinkedList<TreeSide>, Float> single = MINIMAX_SEARCH.search(SINGLE_BRANCH);
        assert single.getValue() == 2.0f;
        assert Arrays.equals(single.getKey().toArray(), new TreeSide[]{TreeSide.RIGHT});
    }

    private void treeSearch() {
        Pair<LinkedList<TreeSide>, Float> single = MINIMAX_SEARCH.search(BIG_TREE);
        assert single.getValue() == 3.0f;
        assert Arrays.equals(single.getKey().toArray(), new TreeSide[]{TreeSide.LEFT, TreeSide.RIGHT});
    }
}
