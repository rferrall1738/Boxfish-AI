package dots.foureighty.lines;

public class Line {
    private final int x; // x of top left most dot
    private final int y; // y of top left most dot
    private final PlayedLine.Direction direction;

    public Line(int x, int y, PlayedLine.Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }
    public enum Direction {
        DOWN, RIGHT
    }

    @Override
    public String toString() {
        return "Line{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return getX() == line.getX() && getY() == line.getY() && getDirection() == line.getDirection();
    }
}
