import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import algorithms.minimax.BinaryTreeMinimaxSearch;
import dots.foureighty.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void singleBranchSearch() {
        Pair<LinkedList<TreeSide>, Float> single = MINIMAX_SEARCH.search(SINGLE_BRANCH);
        assertEquals(2.0f, single.getValue());
        assertArrayEquals(new TreeSide[]{TreeSide.RIGHT}, single.getKey().toArray());
    }

    @Test
    public void treeSearch() {
        Pair<LinkedList<TreeSide>, Float> single = MINIMAX_SEARCH.search(BIG_TREE);

        assertEquals(3.0f, single.getValue());

        assertArrayEquals(new TreeSide[]{TreeSide.LEFT, TreeSide.RIGHT}, single.getKey().toArray());
    }
}
