import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import algorithms.minimax.BinaryTreeAlphaBetaSearch;
import dots.foureighty.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlphaBetaTest {

    private static final BinaryTreeNode SINGLE_NODE = new BinaryTreeValue(1);
    private static final BinaryTreeNode SINGLE_BRANCH = new BinaryTreeBranch(1, 2);
    private static final BinaryTreeNode BIG_TREE =
            new BinaryTreeBranch(new BinaryTreeBranch(new BinaryTreeBranch(8, 2), 3), new BinaryTreeBranch(9, -1));

    private static final BinaryTreeAlphaBetaSearch ALPHA_BETA_SEARCH = new BinaryTreeAlphaBetaSearch();


    @Test
    public void singleNodeSearch() {
        assertEquals(new Pair<>(new LinkedList(), 1.0f), ALPHA_BETA_SEARCH.search(SINGLE_NODE));
    }

    @Test
    public void singleBranchSearch() {
        Pair<LinkedList<TreeSide>, Float> single = ALPHA_BETA_SEARCH.search(SINGLE_BRANCH);
        assertEquals(2.0f, single.getValue());
        assertArrayEquals(new TreeSide[]{TreeSide.RIGHT}, single.getKey().toArray());
    }

    @Test
    public void treeSearch() {
        Pair<LinkedList<TreeSide>, Float> single = ALPHA_BETA_SEARCH.search(BIG_TREE);
        assertEquals(3.0f, single.getValue());

        assertArrayEquals(new TreeSide[]{TreeSide.LEFT, TreeSide.RIGHT}, single.getKey().toArray());
    }
}
