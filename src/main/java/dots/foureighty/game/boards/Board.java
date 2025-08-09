package dots.foureighty.game.boards;


import dots.foureighty.exceptions.UnsupportedBoardSizeException;
import dots.foureighty.lines.BoxSide;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;
import dots.foureighty.lines.Move;
import dots.foureighty.util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private final byte xSize;
    private final byte ySize;
    private final BitSet encodedBoard;
    private final static HashMap<Pair<Byte, Byte>, BitSet> UNPLAYABLE_POSITIONS = new HashMap<>();

    private static BitSet computeUnplayablePositions(byte xSize, byte ySize) {
        Board referenceBoard = new Board(xSize, ySize);
        BitSet unplayablePositions = new BitSet((xSize * ySize) << 1);

        for (int i = 0; i < xSize; i++) {
            unplayablePositions.set(referenceBoard.getIndexFromLine(new Line(i, ySize - 1, LineDirection.DOWN)));
        }
        for (int i = 0; i < ySize; i++) {
            unplayablePositions.set(referenceBoard.getIndexFromLine(new Line(xSize - 1, i, LineDirection.RIGHT)));
        }
        UNPLAYABLE_POSITIONS.put(new Pair<>(xSize, ySize), unplayablePositions);
        return unplayablePositions;
    }
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
     *              00100011 may correspond to a 2x2 grid with the top, left, and bottom line filled.
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
        int bitSetLength = (xSize * ySize << 1);
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
        //Move down the grid until you reach the correct row.
        // Each row that has been passed has (2x-1) lines. (Each dot can have a line down or to the right,
        // the last dot in the row can only go down)
        int index = (getXSize() << 1) * line.getY();

        //Move across the row to the desired column.
        index += ((long) line.getX() << 1);
        if (line.getDirection() == LineDirection.RIGHT) {
            index += 1;
        }
        return index;
    }
    /***
     * Convert an index to a line object
     * @param index index of the bit in the bitset
     * @return A {@link Line} object representation of the index
     */
    public Line getLineFromIndex(int index) {
        if (index < 0 || index >= 2 * getYSize() * getXSize()) {
            return null;
        }
        int row = index / (2 * getXSize());
        int col = index % (2 * getXSize());

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
     * @param index Index of the line to be played
     * @return The directions where a box is completed
     * @see BoxSide
     * @see Board#getCompletedBoxes(int)
     * @see Board#completesBox(Line)
     */

    public EnumSet<BoxSide> getCompletedBoxes(int index) {
        EnumSet<BoxSide> boxes = EnumSet.noneOf(BoxSide.class);
        boolean isVertical = index % 2 == 0;
        if (isVertical) {
            if (completesBoxRight(index)) {
                boxes.add(BoxSide.RIGHT);
            }
            if (completesBoxLeft(index)) {
                boxes.add(BoxSide.LEFT);
            }
        } else {
            if (completesBoxAbove(index)) {
                boxes.add(BoxSide.ABOVE);
            }
            if (completesBoxBelow(index)) {
                boxes.add(BoxSide.BELOW);
            }
        }

        return boxes;

    }

    /***
     * Checks if adding a line completes a box
     * @param line the line to play
     * @return if a box is completed
     * @see Board#getCompletedBoxes(int)
     */
    public boolean completesBox(Line line) {
        return !getCompletedBoxes(line).isEmpty();
    }

    /***
     * Checks if adding a line completes a box
     * @param index the line to play
     * @return if a box is completed
     * @see Board#getCompletedBoxes(int)
     */
    public boolean completesBox(int index) {
        return !getCompletedBoxes(index).isEmpty();
    }

    /***
     * Get the boxes that are completed when playing a line.
     * @param lineToPlay The line to play
     * @return The directions where a box is completed
     * @see BoxSide
     * @see Board#getCompletedBoxes(int)
     */
    public EnumSet<BoxSide> getCompletedBoxes(Line lineToPlay) {
        return getCompletedBoxes(getIndexFromLine(lineToPlay));
    }

    private boolean completesBoxBelow(int index) {
        if (index >= ((ySize - 1) * xSize) << 1) return false;

        //Box below happens when current dot has a down, dot to the right has a down, and the dot below has a right.
        // Mask: 101...1 where the 0 is the current dot, and the last one is a right move for the dot directly below
        boolean leftLine = encodedBoard.get(index - 1);
        boolean rightLine = encodedBoard.get(index + 1);
        boolean bottomLine = encodedBoard.get(index + (xSize << 1));

        return leftLine && rightLine && bottomLine;
    }

    private boolean completesBoxAbove(int index) {
        if (index <= (xSize << 1)) return false;
        int indexAbove = index - (xSize << 1) - 1;
        boolean leftSide = encodedBoard.get(indexAbove);
        boolean topSide = encodedBoard.get(indexAbove + 1);
        boolean rightSide = encodedBoard.get(indexAbove + 2);
        return leftSide && topSide && rightSide;
    }

    private boolean completesBoxRight(int index) {
        if ((index + 1) % (xSize << 1) == 0) return false;
        //Box right happens when current dot is right, dot to the right is down. and dot directly below is right


        boolean topSide = encodedBoard.get(index + 1);
        boolean rightSide = encodedBoard.get(index + 2);
        boolean bottomSide = encodedBoard.get(index + (xSize << 1) + 1);

        return topSide && rightSide && bottomSide;
    }

    private boolean completesBoxLeft(int index) {
        if (index % (xSize << 1) == 0 || index >= ySize * (xSize << 1)) return false;
        //Box left happens when dot to the left is down and right. And the box to the left and down is right.
        boolean leftSide = encodedBoard.get(index - 2);
        boolean topSide = encodedBoard.get(index - 1);
        boolean bottom = encodedBoard.get(index - 1 + (xSize << 1));

        return leftSide && topSide && bottom;
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
        inverseLines.flip(0, (xSize * ySize) << 1);
        inverseLines.andNot(UNPLAYABLE_POSITIONS.getOrDefault(new Pair<>(xSize, ySize), computeUnplayablePositions(xSize, ySize)));
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
     * Clones the board and adds the line to the board
     * @param lineIndex The line to add
     * @return a new board with the added line
     */
    public Board append(int lineIndex) {
        BitSet newBoard = getLineBitSet();
        newBoard.set(lineIndex);
        return new Board(this.xSize, this.ySize, newBoard);
    }

    /***
     * Clones the board and adds the moves to the board
     * @param move The move to add
     * @return a new board with the added line
     */
    public Board append(Move move) {
        BitSet newBoard = getLineBitSet();
        move.getLines().forEach(line -> newBoard.set(getIndexFromLine(line)));
        return new Board(this.xSize, this.ySize, newBoard);
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
            for (int j = 0; j < 2 * getXSize() - 1; j++) {
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

    /**
     * getter for boxes on board
     *
     * @return list of all current boxes on board, the point is top left of box
     */
    public List<Point> getBoxes() {
        List<Point> boxes = new LinkedList<>();
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                boxes.add(new Point(x, y));
            }
        }
        return boxes;
    }

    /**
     * Lines that can make a box
     *
     * @param box
     * @return a list with the lines that form a box
     */
    public List<Line> getBoxesLines(Point box) {
        int x = box.x;
        int y = box.y;
        return Arrays.asList(new Line(x, y, LineDirection.RIGHT),
                new Line(x, y, LineDirection.DOWN),
                new Line(x + 1, y, LineDirection.DOWN),
                new Line(x, y + 1, LineDirection.RIGHT)
        );
    }
}
