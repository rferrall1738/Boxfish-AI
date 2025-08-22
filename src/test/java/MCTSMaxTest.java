import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import algorithms.minimax.BinaryTreeMCTSMaxSearch;
import dots.foureighty.players.robots.algorithms.mcts.MCTSSearchAlgorithm;
import dots.foureighty.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;




public class MCTSMaxTest {
    private static final BinaryTreeNode SINGLE_NODE = new BinaryTreeValue(1);
    private static final BinaryTreeNode SINGLE_BRANCH = new BinaryTreeBranch(1, 2);
    private static final BinaryTreeNode BIG_TREE =
            new BinaryTreeBranch(new BinaryTreeBranch(new BinaryTreeBranch(8, 2), 3), new BinaryTreeBranch(9, -1));




    private static final BinaryTreeMCTSMaxSearch MCTS_MAX_SEARCH = new BinaryTreeMCTSMaxSearch (new MCTSSearchAlgorithm<>(1000), 4, 0.5);



    @Test
    public void singleNodeSearch() {
        Pair<LinkedList<TreeSide>, Float> res = MCTS_MAX_SEARCH.search(SINGLE_NODE);

        assertTrue(res.getKey().isEmpty());
        assertEquals(1.0f, res.getValue(), 1e-6);
    }

    @Test
    public void singleBranchSearch() {
        Pair<LinkedList<TreeSide>, Float> result = MCTS_MAX_SEARCH.search(SINGLE_BRANCH);

        assertNotNull(result.getKey());
        assertFalse(result.getKey().isEmpty());
        assertEquals(TreeSide.RIGHT, result.getKey().getFirst());
        assertEquals(2.0f, result.getValue(), 1e-2);
    }
    @Test
    public void treeSearch() {
        Pair<LinkedList<TreeSide>, Float> result = MCTS_MAX_SEARCH.search(BIG_TREE);

        System.out.println("MCTS Result Value: " + result.getValue());
        System.out.println("MCTS Move Sequence: " + result.getKey());

        assertNotNull(result.getKey());
        assertFalse(result.getKey().isEmpty());


        TreeSide firstMove = result.getKey().getFirst();
        assertTrue(firstMove == TreeSide.LEFT || firstMove == TreeSide.RIGHT);

        assertTrue(result.getValue() >= -1.0f && result.getValue() <= 9.0f);
    }

}

