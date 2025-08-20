package algorithms.binarytree;

import java.util.Random;

public class RandomBinaryTree extends BinaryTreeBranch {

    public static final Random RANDOM = new Random();

    /**
     * Makes a binary tree with values in the range of 2^32 possibilities, and a defined branching probability
     * @param branchingProbability The probability a node results in a branch
     */
    public RandomBinaryTree(float branchingProbability) {
        this(branchingProbability, 500);
    }
    /**
     * Makes a binary tree with values in the range of 2^32 possibilities, and a defined branching probability
     * @param branchingProbability The probability a node results in a branch
     * @param maxDepth of the tree
     */
    public RandomBinaryTree(float branchingProbability, int maxDepth) {
        super(generateChild(branchingProbability, maxDepth), generateChild(branchingProbability, maxDepth));
    }

    private static BinaryTreeNode generateChild(float branchingProbability, int depth) {
        boolean branch = RANDOM.nextFloat() < branchingProbability;

        if (branch && depth > 0) {
            return new RandomBinaryTree(branchingProbability, depth - 1);
        } else {
            return new BinaryTreeValue(RANDOM.nextInt());
        }
    }
}
