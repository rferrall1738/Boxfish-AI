package algorithms.minimax;

import algorithms.binarytree.BinaryTreeBranch;
import algorithms.binarytree.BinaryTreeNode;
import algorithms.binarytree.BinaryTreeValue;
import algorithms.binarytree.TreeSide;
import dots.foureighty.util.Pair;
import dots.foureighty.util.SkippableIterator;

import java.util.NoSuchElementException;

public class SkippableTreeIterator extends SkippableIterator<Pair<BinaryTreeNode, TreeSide>> {
    private final BinaryTreeNode node;
    private boolean hasNext = true;
    private boolean givenLeft = false;

    public SkippableTreeIterator(BinaryTreeNode input) {
        node = input;
        if (input instanceof BinaryTreeValue) {
            hasNext = false;
        }
    }

    @Override
    public void pruneCurrentBranch() throws NoSuchElementException {
        if (node instanceof BinaryTreeValue) {
            throw new NoSuchElementException();
        }
        hasNext = false;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Pair<BinaryTreeNode, TreeSide> next() {
        BinaryTreeBranch branch = (BinaryTreeBranch) node;
        if (!givenLeft) {
            givenLeft = true;
            return new Pair<>(branch.getLeft(), TreeSide.LEFT);
        }
        hasNext = false;
        return new Pair<>(branch.getRight(), TreeSide.RIGHT);
    }
}
