package dots.foureighty.game.boards;

import com.sun.istack.internal.NotNull;
import dots.foureighty.exceptions.UnsupportedBoardSizeException;
import dots.foureighty.lines.BoxSide;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;

import java.awt.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class Board {
    private final byte xSize;
    private final byte ySize;
    private final BitSet encodedBoard;


    /***
     * Creates an empty board.
     * @param xSize number of horizontal dots
     * @param ySize number of vertical dots
     */
    Board(int xSize, int ySize) {
        this(xSize, ySize, new BitSet((xSize * ySize << 1) - xSize - ySize));
    }
    /***
     * Creates a board with existing data on placed encodedBoard
     * @param xSize number of horizontal dots
     * @param ySize number of vertical dots
     * @param encodedBoard Bitset of the board state. The first 2 bits correspond to a line placed in the top left corner.
     *              The second 2 bits correspond to the next 2 bits, sweeping right then down the board.
     *              The first bit is a vertical line, the second is a horizontal line. If a line is not possible,
     *              the bit is skipped.
     *              <p>
     *              1011 may correspond to a 2x2 grid with the top, left, and bottom line filled.
     *              </p>
     * @see Board#Board(int, int)  Board
     * @throws UnsupportedBoardSizeException If the board dimensions are equal to 0 or greater than
     * the size of an unsigned byte
     */
    public Board(int xSize, int ySize, BitSet encodedBoard) {
        this((byte) xSize, (byte) ySize, encodedBoard);
    }

    private Board(byte xSize, byte ySize, BitSet encodedBoard) {
        this.xSize = xSize;
        this.ySize = ySize;

        //Remove unused data
        int bitSetLength = (xSize * ySize << 1) - xSize - ySize;
        BitSet mask = new BitSet(bitSetLength);
        mask.set(0, bitSetLength);
        mask.and(encodedBoard);

        this.encodedBoard = mask;
    }

    public byte getXSize() {
        return xSize;
    }

    public byte getYSize() {
        return ySize;
    }

    /***
     * Checks if the board contains a given line.
     * @param line The line to check for.
     * @return If the line has already been played
     */
    public boolean containsLine(Line line) {
        int index = getIndexFromLine(line);
        if(index == -1) return false;
        return encodedBoard.get(index);
    }

    /***
     * Gets the index of a given line in the encoded board
     * @param line Line to search for
     * @return index of the bit in the bitset
     */
    public int getIndexFromLine(Line line) {
        if(line.getX() > xSize - 1 ||  line.getY() > ySize - 1 || line.getX() < 0 || line.getY() < 0) {
            return -1;
        }
        if(line.getX() == xSize -1 && line.getDirection() == LineDirection.RIGHT ||
                line.getY() == ySize -1 && line.getDirection() == LineDirection.DOWN) {
            return -1;
        }
        //Move down the grid until you reach the correct row.
        // Each row that has been passed has (2x-1) lines. (Each dot can have a line down or to the right,
        // the last dot in the row can only go down)
        long index = (((long) getXSize() << 1) - 1) * line.getY();

        //Move across the row to the desired column.
        index += ((long) line.getX() << 1);
        if (line.getY() + 1 == getYSize()) {
            index -= line.getX(); //Last row does not have any vertical lines.
        }else if (line.getDirection() == LineDirection.RIGHT) {
            index += 1; //First bit is down. Second is right.
        }
        return (int) index;
    }

    /***
     * Convert an index to a line object
     * @param index index of the bit in the bitset
     * @return A {@link Line} object representation of the index
     */
    public Line getLineFromIndex(int index) {
        if (index < 0 || index > 2 * getYSize() * getXSize() - getXSize() - getYSize()) {
            return null;
        }
        int row = index / (2 * getXSize() -  1);
        int col = index % (2 * getXSize() -  1);
        if (row + 1 == getYSize()) {
            return new Line(col,row, LineDirection.RIGHT);
        }
        LineDirection direction = col % 2 == 1 ? LineDirection.RIGHT : LineDirection.DOWN;
        return  new Line(col / 2, row, direction);

    }

    /**
     * Finds all the lines that haven't been played
     * @return A list of lines that haven't been played
     */
    public ArrayList<Line> getUnplayedPositions() {
        return (ArrayList<Line>) getInverseBitSet().stream().mapToObj(this::getLineFromIndex).collect(Collectors.toList());
    }

    /***
     * Get the boxes that are completed when playing a line.
     * @param line The line that is played
     * @return The directions where a box is completed
     * @see BoxSide
     * @see Board#getCompletedBoxes(int)
     * @see Board#completesBox(Line)
     */
    @NotNull
    public EnumSet<BoxSide> getCompletedBoxes(Line line) {
        EnumSet<BoxSide> boxes = EnumSet.noneOf(BoxSide.class);

        if (line.getDirection() == LineDirection.RIGHT) {
            if(completesBoxAbove(line)) {
                boxes.add(BoxSide.ABOVE);
            }
            if (completesBoxBelow(line)) {
                boxes.add(BoxSide.BELOW);
            }
        } else {
            if (completesBoxRight(line)) {
                boxes.add(BoxSide.RIGHT);
            }
            if (completesBoxLeft(line)) {
                boxes.add(BoxSide.LEFT);
            }
        }

        return boxes;

    }

    /***
     * Checks if adding a line completes a box
     * @param line the line to play
     * @return if a box is completed
     * @see Board#getCompletedBoxes(Line)
     */
    public boolean completesBox(Line line) {
        return !getCompletedBoxes(line).isEmpty();
    }

    /***
     * Get the boxes that are completed when playing a line.
     * @param indexOfLine The index of the line in the bitset
     * @return The directions where a box is completed
     * @see BoxSide
     * @see Board#getCompletedBoxes(Line)
     */
    public EnumSet<BoxSide> getCompletedBoxes(int indexOfLine) {
        return getCompletedBoxes(getLineFromIndex(indexOfLine));
    }

    private boolean completesBoxBelow(Line newLine) {
        if (newLine.getY() + 1 >= getYSize()) return false;

        //Box below happens when current dot has a down, dot to the right has a down, and the dot below has a right.
        // Mask: 101...1 where the 0 is the current dot, and the last one is a right move for the dot directly below

        Line lineBelow = new Line(newLine.getX(), newLine.getY() + 1, LineDirection.RIGHT);
        int indexOfDot = getIndexFromLine(newLine);
        BitSet mask = new BitSet(encodedBoard.length());
        mask.set(indexOfDot - 1); //Down line on the dot
        mask.set(indexOfDot + 1); // Down line on the dot to the right
        mask.set(getIndexFromLine(lineBelow)); //Right line on the dot below

        mask.andNot(encodedBoard);
        return mask.cardinality() == 0;
    }
    private boolean completesBoxAbove(Line newLine) {
        if (newLine.getY() == 0) return false;

        //Box above happens at DOWN and RIGHT for dot directly above point, then Down for the dot to the right of that one
        // Mask: 111 at the dot directly above the current index
        Line lineAbove = new Line(newLine.getX(), newLine.getY() - 1, LineDirection.DOWN);
        int indexOfDotAbove = getIndexFromLine(lineAbove);

        BitSet mask = new BitSet(encodedBoard.length());
        mask.set(indexOfDotAbove,indexOfDotAbove + 3);
        mask.andNot(encodedBoard);
        return mask.cardinality() == 0;
    }
    private boolean completesBoxLeft(Line newLine) {
        if (newLine.getX() == 0) return false;
        //Box left happens when dot to the left is down and right. And the box to the left and down is right.
        Line dotLeftDown =  new Line(newLine.getX() - 1, newLine.getY() + 1, LineDirection.RIGHT);
        BitSet mask = new BitSet(encodedBoard.length());
        int currentDotIndex = getIndexFromLine(newLine);
        mask.set(currentDotIndex - 2, currentDotIndex); //Dot to the left is down and right
        mask.set(getIndexFromLine(dotLeftDown)); //Dot down and left is right
        mask.andNot(encodedBoard);

        return mask.cardinality() == 0;
    }
    private boolean completesBoxRight(Line newLine) {
        if (newLine.getX() +1 >= getXSize()) return false;
        //Box right happens when current dot is right, dot to the right is down. and dot directly below is right
        Line dotBelow =  new Line(newLine.getX(), newLine.getY() + 1, LineDirection.RIGHT);

        int indexOfDot = getIndexFromLine(newLine);
        BitSet mask = new BitSet(encodedBoard.length());
        mask.set(indexOfDot + 1, indexOfDot + 3);
        mask.set(getIndexFromLine(dotBelow));
        mask.andNot(encodedBoard);

        return mask.cardinality() == 0;
    }

    /***
     * Get the boxes that will be completed after playing a line.
     * Points are the coordinate of the dot at the top left of the box.
     * @param line the line to play
     * @return Locations of boxes that will be completed after placing a line
     */
    public ArrayList<Point> getCompletedBoxLocations(Line line) {
        ArrayList<Point> points = new ArrayList<>();
        EnumSet<BoxSide> boxes = getCompletedBoxes(line);
        if (boxes.contains(BoxSide.LEFT)) {
            points.add(new Point(line.getX() - 1, line.getY()));
        }
        if (boxes.contains(BoxSide.RIGHT) || boxes.contains(BoxSide.BELOW)) {
            points.add(new Point(line.getX(), line.getY()));
        }
        if (boxes.contains(BoxSide.ABOVE)) {
            points.add(new Point(line.getX(), line.getY() - 1));
        }

        return points;
    }

    /***
     * Get a copy of the board bitset
     * @return Clone of the board's bitset
     */
    public BitSet getLineBitSet() {
        return (BitSet) encodedBoard.clone();
    }

    /***
     * Get a copy of the board bitset
     * @return Clone of the board's bitset
     */
    public BitSet getInverseBitSet() {
        BitSet inverseLines = (BitSet) encodedBoard.clone();
        inverseLines.flip(0, 2 * xSize * ySize - xSize - ySize);
        return inverseLines;
    }

    @Override
    public Board clone() {
        return new Board(this.xSize,this.ySize,getLineBitSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(xSize, ySize, encodedBoard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.hashCode() == o.hashCode();
    }

    /***
     * Clones the board and adds the line to the board
     * @param line The line to add
     * @return a new board with the added line
     */
    public Board append(Line line) {
        int index = getIndexFromLine(line);
        BitSet newBoard = getLineBitSet();
        newBoard.set(index);
        return new Board(this.xSize,this.ySize,newBoard);
    }

    /***
     * Clones the board and removes a line from the board
     * @param line The line to remove
     * @return a new board with the line removed
     */
    public Board remove(Line line) {
        int index = getIndexFromLine(line);
        BitSet newBoard = getLineBitSet();
        newBoard.set(index, 0);
        return new Board(this.xSize, this.ySize, newBoard);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("xSize=").append(xSize).append(", ySize=").append(ySize);
        sb.append("\n");
        for (int i = 0; i < 2 * getYSize() - 1; i++) {
            for (int j = 0; j < 2 *getXSize() - 1; j++) {
                if(i % 2 == 0){
                    if(j % 2 == 0) sb.append("*");
                    else if (containsLine(new Line((j -1) / 2,i / 2,LineDirection.RIGHT))) sb.append("-");
                    else sb.append(" ");
                } else {
                    if(j % 2 == 1) sb.append(" ");
                    else if (containsLine(new Line(j / 2,(i- 1) / 2,LineDirection.DOWN))) sb.append("|");
                    else sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
