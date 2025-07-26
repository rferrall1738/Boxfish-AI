package dots.foureighty.lines;

import java.util.ArrayList;

public class Move {
    private final ArrayList<Line> lines;

    public Move(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

}
