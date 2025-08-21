package algorithms.binarytree;

import java.util.Random;

public class RandomBinaryTree extends BinaryTreeBranch {

    public static final Random RANDOM = new Random();

    /**
     * Makes a binary tree with values in the range of 2^32 possibilities, and a defined branching probability
     * @param branchingProbability The probability a node results in a branch
     */
    public RandomBinaryTree(float branchingProbability) {
        super(generateChild(branchingProbability), generateChild(branchingProbability));
    }

    private static BinaryTreeNode generateChild(float branchingProbability) {
        boolean branch = RANDOM.nextFloat() < branchingProbability;

        if (branch) {
            return new RandomBinaryTree(branchingProbability);
        } else {
            return new BinaryTreeValue(RANDOM.nextInt());
        }
    }
}