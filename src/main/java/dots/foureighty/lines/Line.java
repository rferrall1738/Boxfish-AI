package dots.foureighty.lines;

import java.util.Objects;

public class Line {
    private final int x; // x of top left most dot
    private final int y; // y of top left most dot

    private final LineDirection direction;


    public Line(int x, int y, LineDirection direction) {
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

    public LineDirection getDirection() {
        return direction;
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
    public int hashCode() {
        return Objects.hash(getX(), getY(), getDirection());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.hashCode() == o.hashCode();
    }
}
