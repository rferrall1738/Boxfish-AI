package dots.foureighty.lines;

import java.util.Arrays;
import java.util.LinkedList;

public class Move {
    private final LinkedList<Line> lines;

    public Move(LinkedList<Line> lines) {
        this.lines = lines;
    }

    public LinkedList<Line> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return "Move [lines=" + Arrays.toString(lines.toArray()) + "]";
    }
}
