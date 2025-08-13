import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import algorithms.minimax.BinaryTreeMCTSSearch;
import dots.foureighty.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class MCTSTest {

    private static final BinaryTreeNode SINGLE_NODE = new BinaryTreeValue(1);
    private static final BinaryTreeNode SINGLE_BRANCH = new BinaryTreeBranch(1, 2);
    private static final BinaryTreeNode BIG_TREE = new BinaryTreeBranch(
            new BinaryTreeBranch(new BinaryTreeBranch(8, 2), 3),
            new BinaryTreeBranch(9, -1)
    );

    private static final BinaryTreeMCTSSearch MCTS_SEARCH = new BinaryTreeMCTSSearch(1000);

    @Test
    public void singleNodeSearch() {
        Pair<LinkedList<TreeSide>, Float> result = MCTS_SEARCH.search(SINGLE_NODE);
        assertEquals(1.0f, result.getValue());
        assertTrue(result.getKey().isEmpty());
    }

    @Test
    public void singleBranchSearch() {
        Pair<LinkedList<TreeSide>, Float> result = MCTS_SEARCH.search(SINGLE_BRANCH);

        assertNotNull(result.getKey());
        assertFalse(result.getKey().isEmpty());

        System.out.println("MCTS Result Value: " + result.getValue());
        System.out.println("MCTS Move Sequence: " + result.getKey());

        TreeSide firstMove = result.getKey().getFirst();
        assertTrue(firstMove == TreeSide.LEFT || firstMove == TreeSide.RIGHT);

        // The value should be between 1 and 2
        assertTrue(result.getValue() >= 1.0f && result.getValue() <= 2.0f);
    }

    @Test
    public void treeSearch() {
        Pair<LinkedList<TreeSide>, Float> result = MCTS_SEARCH.search(BIG_TREE);

        System.out.println("MCTS Result Value: " + result.getValue());
        System.out.println("MCTS Move Sequence: " + result.getKey());

        assertNotNull(result.getKey());
        assertFalse(result.getKey().isEmpty());


        TreeSide firstMove = result.getKey().getFirst();
        assertTrue(firstMove == TreeSide.LEFT || firstMove == TreeSide.RIGHT);

         assertTrue(result.getValue() >= -1.0f && result.getValue() <= 9.0f);
    }

}