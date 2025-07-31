package algorithms.binarytree;

public class BinaryTreeValue implements BinaryTreeNode {
    private final int value;

    public BinaryTreeValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
