import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.RandomBinaryTree;
import algorithms.minimax.BinaryTreeAlphaBetaSearch;
import algorithms.minimax.BinaryTreeMinimaxSearch;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


/***
 * Makes sure alphabeta search and minimax is returning the same result.
 */
public class SearchSimilarityTest {

    private static final BinaryTreeMinimaxSearch MINIMAX_SEARCH = new BinaryTreeMinimaxSearch();
    private static final BinaryTreeAlphaBetaSearch ALPHA_BETA_SEARCH = new BinaryTreeAlphaBetaSearch();

    @Test
    public void testSmallTree() throws InterruptedException {
        BinaryTreeNode tree = new RandomBinaryTree(0.05f);
        assertEquals(MINIMAX_SEARCH.search(tree), ALPHA_BETA_SEARCH.search(tree));
    }
    @Test
    public void testMediumTree() throws InterruptedException {
        BinaryTreeNode tree = new RandomBinaryTree(0.3f);
        assertEquals(MINIMAX_SEARCH.search(tree), ALPHA_BETA_SEARCH.search(tree));
    }
    @Test
    public void testLargeTree() throws InterruptedException {
        BinaryTreeNode tree = new RandomBinaryTree(0.5f);
        assertEquals(MINIMAX_SEARCH.search(tree), ALPHA_BETA_SEARCH.search(tree));
    }
}
