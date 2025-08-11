import dots.foureighty.exceptions.UnsupportedBoardSizeException;
import dots.foureighty.game.boards.Board;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.lines.BoxSide;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTests {

    @Test
    public void zeroByZeroBoard() {
        assertThrows(UnsupportedBoardSizeException.class, () -> StandardBoards.AMERICAN.generateBoard(0, 0));
    }

    @Test
    public void negativeBoardSize() {
        assertThrows(UnsupportedBoardSizeException.class, () -> StandardBoards.AMERICAN.generateBoard(-1, 0));
    }

    @Test
    public void boardTooBig() {
        assertThrows(UnsupportedBoardSizeException.class, () -> StandardBoards.AMERICAN.generateBoard(510, 100));
    }


    @Test
    public void initFromBitstring() {
        Board dummyBoard = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100011}));
        assertEquals(0, dummyBoard.getIndexFromLine(new Line(0, 0, LineDirection.DOWN)));

        assertTrue(dummyBoard.containsLine(new Line(0, 0, LineDirection.DOWN)));
        assertTrue(dummyBoard.containsLine(new Line(0, 0, LineDirection.RIGHT)));
        assertFalse(dummyBoard.containsLine(new Line(1, 0, LineDirection.DOWN)));
        assertTrue(dummyBoard.containsLine(new Line(0, 1, LineDirection.RIGHT)));


        assertFalse(dummyBoard.containsLine(new Line(1, 0, LineDirection.RIGHT)));
        assertFalse(dummyBoard.containsLine(new Line(1, 1, LineDirection.RIGHT)));
        assertFalse(dummyBoard.containsLine(new Line(1, 1, LineDirection.DOWN)));
        assertFalse(dummyBoard.containsLine(new Line(0, 1, LineDirection.DOWN)));

    }

    @Test
    public void checkLineToIndex() {
        Board board = StandardBoards.AMERICAN.generateBoard(3, 3);
        assertEquals(0, board.getIndexFromLine(new Line(0, 0, LineDirection.DOWN)));
        assertEquals(1, board.getIndexFromLine(new Line(0, 0, LineDirection.RIGHT)));
        assertEquals(3, board.getIndexFromLine(new Line(1, 0, LineDirection.RIGHT)));
        assertEquals(4, board.getIndexFromLine(new Line(2, 0, LineDirection.DOWN)));
        assertEquals(5, board.getIndexFromLine(new Line(2, 0, LineDirection.RIGHT)));
        assertEquals(8, board.getIndexFromLine(new Line(1, 1, LineDirection.DOWN)));
        assertEquals(15, board.getIndexFromLine(new Line(1, 2, LineDirection.RIGHT)));
        assertEquals(17, board.getIndexFromLine(new Line(2, 2, LineDirection.RIGHT)));
    }

    @Test
    public void outOfBoundsIndexing() {
        Board board = StandardBoards.AMERICAN.generateBoard(3, 3);
        assertEquals(-1, board.getIndexFromLine(new Line(3, 2, LineDirection.DOWN)));
        assertEquals(-1, board.getIndexFromLine(new Line(-1, -1, LineDirection.DOWN)));
        assertEquals(-1, board.getIndexFromLine(new Line(3, 0, LineDirection.DOWN)));
        assertEquals(-1, board.getIndexFromLine(new Line(2, 3, LineDirection.DOWN)));

    }

    @Test
    public void checkIndexToLine() {
        Board board = StandardBoards.AMERICAN.generateBoard(3, 3);

        assertEquals(new Line(0, 0, LineDirection.DOWN), board.getLineFromIndex(0));
        assertEquals(new Line(0, 0, LineDirection.RIGHT), board.getLineFromIndex(1));
        assertEquals(new Line(1, 0, LineDirection.RIGHT), board.getLineFromIndex(3));
        assertEquals(new Line(2, 0, LineDirection.DOWN), board.getLineFromIndex(4));
        assertEquals(new Line(0, 1, LineDirection.RIGHT), board.getLineFromIndex(7));
        assertEquals(new Line(2, 1, LineDirection.RIGHT), board.getLineFromIndex(11));
    }

    @Test
    public void lineEqualities() {
        assertEquals(new Line(0, 0, LineDirection.DOWN), new Line(0, 0, LineDirection.DOWN));
        assertEquals(new Line(0, 0, LineDirection.RIGHT), new Line(0, 0, LineDirection.RIGHT));
        assertNotEquals(new Line(1, 0, LineDirection.DOWN), new Line(1, 0, LineDirection.RIGHT));
    }

    @Test
    public void confirmInvertibleIndexing() {
        Board board = StandardBoards.AMERICAN.generateBoard(3, 3);
        Line topLeftDown = new Line(0,0,LineDirection.DOWN);
        Line topRightDown = new Line(2,1,LineDirection.DOWN);
        Line bottomLeftRight = new Line(1,2,LineDirection.DOWN);

        assertEquals(topLeftDown, board.getLineFromIndex(board.getIndexFromLine(topLeftDown)));
        assertEquals(topRightDown, board.getLineFromIndex(board.getIndexFromLine(topRightDown)));
        assertEquals(bottomLeftRight, board.getLineFromIndex(board.getIndexFromLine(bottomLeftRight)));
    }

    @Test
    public void completeBoxOnLeft() {
        Board missingRight = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100011}));
        assertEquals(EnumSet.of(BoxSide.LEFT), missingRight.getCompletedBoxes(new Line(1, 0, LineDirection.DOWN)));
    }

    @Test
    public void completeBoxOnRight() {
        Board missingLeft = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100110}));
        assertEquals(EnumSet.of(BoxSide.RIGHT), missingLeft.getCompletedBoxes(new Line(0, 0, LineDirection.DOWN)));
    }

    @Test
    public void completeBoxAbove() {
        Board missingBottom = new Board(2, 2, BitSet.valueOf(new byte[]{0b00000111}));
        assertEquals(EnumSet.of(BoxSide.ABOVE), missingBottom.getCompletedBoxes(new Line(0, 1, LineDirection.RIGHT)));
    }

    @Test
    public void completeBoxBelow() {
        Board missingTop = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100101}));
        assertEquals(EnumSet.of(BoxSide.BELOW), missingTop.getCompletedBoxes(new Line(0, 0, LineDirection.RIGHT)));
    }


}
