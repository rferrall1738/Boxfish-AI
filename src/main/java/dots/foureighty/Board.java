package dots.foureighty;

import dots.foureighty.lines.Line;
import dots.foureighty.lines.PlayedLine;
import java.util.ArrayList;
import java.util.Objects;

public class Board {
    private final int xSize;
    private final int ySize;
    private final ArrayList<PlayedLine> playedLines = new ArrayList<>();
    private final ArrayList<PlayedLine> currentMove = new ArrayList<>();

    public Board(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public ArrayList<PlayedLine> getPlayedLines() {
        return playedLines;
    }

    public void addLine(PlayedLine playedLine) {
        currentMove.add(playedLine);
        playedLines.add(playedLine);
    }

    public ArrayList<Line> getValidLinePlacements() {
        ArrayList<Line> validLines = new ArrayList<>();
        for (int i = 0; i < xSize - 1; i++) {
            for (int j = 0; j < ySize; j++) {
                validLines.add(new Line(i, j, Line.Direction.RIGHT));
            }
        }
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize - 1; j++) {
                validLines.add(new Line(i, j, Line.Direction.DOWN));
            }
        }
        playedLines.forEach(line -> {
            validLines.removeIf(validLine -> validLine.equals(new Line(line.getX(), line.getY(), line.getDirection())));
        });
        return validLines;
    }
}
