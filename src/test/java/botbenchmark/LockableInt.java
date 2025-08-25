package botbenchmark;

public class LockableInt {
    private int value;

    public LockableInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
