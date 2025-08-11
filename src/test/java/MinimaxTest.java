import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import algorithms.minimax.BinaryTreeMinimaxSearch;
import dots.foureighty.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MinimaxTest {

    private static final BinaryTreeNode SINGLE_NODE = new BinaryTreeValue(1);
    private static final BinaryTreeNode SINGLE_BRANCH = new BinaryTreeBranch(1, 2);
    private static final BinaryTreeNode BIG_TREE =
            new BinaryTreeBranch(new BinaryTreeBranch(new BinaryTreeBranch(8, 2), 3), new BinaryTreeBranch(9, -1));

    private static final BinaryTreeMinimaxSearch MINIMAX_SEARCH = new BinaryTreeMinimaxSearch();

    @Test
    public void singleNodeSearch() {
        Pair<LinkedList<TreeSide>, Float> singleNodeResults = MINIMAX_SEARCH.search(SINGLE_NODE);

        assertEquals(new Pair<>(new LinkedList(), 1.0f), singleNodeResults);
        assertNotEquals(new Pair<>(new LinkedList(), 2.0f), singleNodeResults);

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
