package dots.foureighty.gamebuilder;

import dots.foureighty.lines.BoxSide;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumSet;

public class Board {
    private final int xSize;
    private final int ySize;
    private final BitSet lines;

    /***
     * Creates a board with existing data on placed lines
     * @param xSize number of horizontal dots
     * @param ySize number of vertical dots
     * @param lines Bitset of the board state. The first 2 bits correspond to a line placed in the top left corner.
     *              The second 2 bits correspond to the next 2 bits, sweeping right then down the board.
     *              The first bit is a vertical line, the second is a horizontal line. If a line is not possible,
     *              the bit is skipped.
     *              <p>
     *              1101 may correspond to a 2x2 grid with the top, left, and bottom line filled.
     *              </p>
     * @see Board#Board(int, int)  Board
     */
    public Board(int xSize, int ySize, BitSet lines) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.lines = lines;
    }

    /***
     * Creates an empty board.
     * @param xSize number of horizontal dots
     * @param ySize number of vertical dots
     */
    public Board(int xSize, int ySize) {
        this(xSize, ySize, new BitSet(2 * xSize * ySize - xSize - ySize));
        if(xSize <= 0 || ySize <= 0 || 2 * xSize * ySize - xSize - ySize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Unsupported board size");
        }
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }


    /***
     * Checks if the lines BitSet has the right number of bits for the board size;
     * @return true if bitset is valid, false if not.
     */
    public boolean isBitSetValid() {
        return lines.length() ==  2 * xSize * ySize - xSize - ySize;
    }

    /***
     * Checks if the board contains a given line.
     * @param line The line to check for.
     * @return If the line has already been played
     */
    public boolean containsLine(Line line) {
        int index = getIndexOfLine(line);
        if(index == -1) return false;
        return lines.get(index);
    }

    /***
     * Converts a line to an index in the bitset
     * @param line line on the board
     * @return index of the bit in the bitset
     */
    public int getIndexOfLine(Line line) {
        if(line.getX() > xSize - 1 ||  line.getY() > ySize - 1 || line.getX() < 0 || line.getY() < 0) {
            return -1;
        }
        //Move down the grid until you reach the correct row.
        // Each row that has been passed has (2x-1) lines. (Each dot can have a line down or to the right,
        // the last dot in the row can only go down)
        long index = (2 * getXSize() - 1) * line.getY();

        //Move across the row to the desired column.
        index += 2 * line.getX();
        if (line.getY() + 1 == getYSize()) {
            index -= line.getX(); //Last row does not have any vertical lines.
        }
        if (line.getDirection() == LineDirection.RIGHT) {
            index += 1; //First bit is down. Second is right.
        }
        return (int) index;
    }

    /***
     * Convert an index to the line that it is corresponding to.
     * @param index index of the bit in the bitset
     * @return The line representation
     */
    public Line convertIndexToLine(int index) {
        if (index < 0 || index > 2 * getYSize() * getXSize() - getXSize() - getYSize()) {}
        int row = index / (2 * getXSize() -  1);
        int col = index % (2 * getXSize() -  1);

        if (row + 1 == getYSize()) {
            return new Line(row,col, LineDirection.RIGHT);
        }
        LineDirection direction = col % 2 == 1 ? LineDirection.RIGHT : LineDirection.DOWN;
        return  new Line(col / 2, row, direction);

    }


    public ArrayList<Line> getValidLinePlacements() {
        BitSet inverseLines = (BitSet) lines.clone();
        inverseLines.flip(0,lines.length());
        ArrayList<Line> validLines = new ArrayList<>();
        //TODO
        return null;
    }
    public EnumSet<BoxSide> getCompletedBoxes(Line line) {
        EnumSet<BoxSide> boxes = EnumSet.noneOf(BoxSide.class);

        //Horizontal lines can only complete boxes above and below.

        if(line.getDirection() == LineDirection.RIGHT) {
            //Check for above
           //TODO
        }

        return boxes;

    }
}
