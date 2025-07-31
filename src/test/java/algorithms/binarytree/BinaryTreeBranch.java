package algorithms.binarytree;

public class BinaryTreeBranch implements BinaryTreeNode {
    private final BinaryTreeNode left;
    private final BinaryTreeNode right;

    public BinaryTreeBranch(BinaryTreeNode left, BinaryTreeNode right) {
        this.left = left;
        this.right = right;
    }

    public BinaryTreeBranch(int left, BinaryTreeNode right) {
        this.left = new BinaryTreeValue(left);
        this.right = right;
    }

    public BinaryTreeBranch(int left, int right) {
        this.left = new BinaryTreeValue(left);
        this.right = new BinaryTreeValue(right);
    }

    public BinaryTreeBranch(BinaryTreeNode left, int right) {
        this.left = left;
        this.right = new BinaryTreeValue(right);
    }

    public BinaryTreeNode getLeft() {
        return left;
    }

    public BinaryTreeNode getRight() {
        return right;
    }
}
