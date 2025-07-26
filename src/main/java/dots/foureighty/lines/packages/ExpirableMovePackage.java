package dots.foureighty.lines.packages;

public class ExpirableMovePackage extends MovePackage {
    private boolean expired = false;

    public void expire() {
        expired = true;
    }

    public boolean isExpired() {
        return expired;
    }
}
