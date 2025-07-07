package dots.foureighty.lines;

import java.awt.*;

public class GUILine extends Line{
    private final Point GUILocation;

    public GUILine(int x, int y, Line.Direction direction, Point GUILocation) {
        super(x, y, direction);
        this.GUILocation = GUILocation;
    }


    public Point getGUILocation() {
        return GUILocation;
    }
}
