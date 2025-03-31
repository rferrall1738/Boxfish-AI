package me.curtisbradley;

import me.curtisbradley.moves.Line;

import java.util.ArrayList;

public class Board {
    private final int xSize;
    private final int ySize;

    private final ArrayList<Line> lines = new ArrayList<>();

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
    public ArrayList<Line> getLines() {
        return lines;
    }
}
