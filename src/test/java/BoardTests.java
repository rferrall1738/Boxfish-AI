import dots.foureighty.exceptions.UnsupportedBoardSizeException;
import dots.foureighty.game.boards.Board;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.lines.BoxSide;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;

import java.util.BitSet;
import java.util.EnumSet;

public class BoardTests {
    public void runTests(){
        badBoardSize();
        boardInit();
        checkLineEqualities();
        checkLineToIndex();
        checkIndexToLine();

        confirmInvertibleIndexing();
        checkContainsLine();
        checkBoxCompletes();
    }

    public void badBoardSize(){
        try {
            StandardBoards.AMERICAN.generateBoard(0, 0);
            throw new AssertionError("Invalid board did not error");
        } catch (UnsupportedBoardSizeException ignore) {
        }
        catch(Exception other) {
            throw new AssertionError("Unexpected exception", other);
        }
        try {
            StandardBoards.AMERICAN.generateBoard(-1, 5);
            throw new AssertionError("Invalid board did not error");
        } catch (UnsupportedBoardSizeException ignore) {
        } catch (Exception other) {
            throw new AssertionError("Unexpected exception", other);
        }
        try {
            StandardBoards.AMERICAN.generateBoard(510, 20);
            throw new AssertionError("Invalid board did not error");
        } catch (UnsupportedBoardSizeException ignore) {
        } catch (Exception other) {
            throw new AssertionError("Unexpected exception", other);
        }

    }
    public void boardInit() {

        StandardBoards.AMERICAN.generateBoard(255, 255);
        new Board(2, 2, BitSet.valueOf(new byte[]{0b1101}));
        new Board(3, 3, BitSet.valueOf(new byte[]{0b1101}));
        new Board(3, 3, BitSet.valueOf(new byte[]{0b1101101, 0b01001}));
    }
    public void checkContainsLine() {
        Board dummyBoard = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100011}));

        assert dummyBoard.getIndexFromLine(new Line(0, 0, LineDirection.DOWN)) == 0;

        assert dummyBoard.containsLine(new Line(0,0, LineDirection.DOWN));
        assert dummyBoard.containsLine(new Line(0,0, LineDirection.RIGHT));
        assert !dummyBoard.containsLine(new Line(1,0, LineDirection.DOWN));
        assert dummyBoard.containsLine(new Line(0,1, LineDirection.RIGHT));


        assert !dummyBoard.containsLine(new Line(1,0, LineDirection.RIGHT));
        assert !dummyBoard.containsLine(new Line(1,1, LineDirection.RIGHT));
        assert !dummyBoard.containsLine(new Line(1,1, LineDirection.DOWN));
        assert !dummyBoard.containsLine(new Line(0,1, LineDirection.DOWN));
    }
    public void checkLineToIndex() {
        Board board = StandardBoards.AMERICAN.generateBoard(3, 3);
        assert board.getIndexFromLine(new Line(0, 0, LineDirection.DOWN)) == 0;
        assert board.getIndexFromLine(new Line(0, 0, LineDirection.RIGHT)) == 1;
        assert board.getIndexFromLine(new Line(1, 0, LineDirection.RIGHT)) == 3;
        assert board.getIndexFromLine(new Line(2, 0, LineDirection.RIGHT)) == 5;
        assert board.getIndexFromLine(new Line(2, 0, LineDirection.DOWN)) == 4;
        assert board.getIndexFromLine(new Line(1, 1, LineDirection.DOWN)) == 8;
        assert board.getIndexFromLine(new Line(1, 2, LineDirection.RIGHT)) == 15;
        assert board.getIndexFromLine(new Line(2, 2, LineDirection.RIGHT)) == 17;
        assert board.getIndexFromLine(new Line(3, 2, LineDirection.RIGHT)) == -1;

    }
    public void checkIndexToLine() {
        Board board = StandardBoards.AMERICAN.generateBoard(3, 3);
        assert board.getLineFromIndex(0).equals(new Line(0, 0, LineDirection.DOWN));
        assert board.getLineFromIndex(1).equals(new Line(0, 0, LineDirection.RIGHT));
        assert board.getLineFromIndex(3).equals(new Line(1, 0, LineDirection.RIGHT));
        assert board.getLineFromIndex(4).equals(new Line(2, 0, LineDirection.DOWN));
        assert board.getLineFromIndex(7).equals(new Line(0, 1, LineDirection.RIGHT));
        assert board.getLineFromIndex(11).equals(new Line(2, 1, LineDirection.RIGHT));
    }

    public void checkLineEqualities() {
        Line line1 = new Line(1,1,LineDirection.RIGHT);
        Line line2 = new Line(2,2,LineDirection.RIGHT);
        Line line3 = new Line(1,1,LineDirection.RIGHT);
        Line line4 = new Line(1,1,LineDirection.DOWN);
        Line line5 = new Line(1,2,LineDirection.RIGHT);
        Line line6 = new Line(2,1,LineDirection.RIGHT);

        assert !line1.equals(line2);
        assert line1.equals(line3);
        assert !line1.equals(line4);
        assert !line1.equals(line5);
        assert !line1.equals(line6);


    }
    public void confirmInvertibleIndexing() {
        Board board = StandardBoards.AMERICAN.generateBoard(3, 3);
        Line topLeftDown = new Line(0,0,LineDirection.DOWN);
        Line topRightDown = new Line(2,1,LineDirection.DOWN);
        Line bottomLeftRight = new Line(1,2,LineDirection.DOWN);
        Line offBoard = new Line(5, 5, LineDirection.RIGHT);

        assert board.getLineFromIndex(board.getIndexFromLine(topLeftDown)).equals(topLeftDown);
        assert board.getLineFromIndex(board.getIndexFromLine(topRightDown)).equals(topRightDown);
        assert board.getLineFromIndex(board.getIndexFromLine(bottomLeftRight)).equals(bottomLeftRight);
        assert board.getLineFromIndex(board.getIndexFromLine(offBoard)) == null;
    }
    public void checkBoxCompletes(){
        Board missingRight = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100011}));
        assert missingRight.getCompletedBoxes(new Line(1, 0, LineDirection.DOWN)).equals(EnumSet.of(BoxSide.LEFT));

        Board missingLeft = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100110}));
        assert missingLeft.getCompletedBoxes(new Line(0, 0, LineDirection.DOWN)).equals(EnumSet.of(BoxSide.RIGHT));

        Board missingTop = new Board(2, 2, BitSet.valueOf(new byte[]{0b00100101}));
        assert missingTop.getCompletedBoxes(new Line(0, 0, LineDirection.RIGHT)).equals(EnumSet.of(BoxSide.BELOW));

        Board missingBottom = new Board(2, 2, BitSet.valueOf(new byte[]{0b00000111}));
        assert missingBottom.getCompletedBoxes(new Line(0, 1, LineDirection.RIGHT)).equals(EnumSet.of(BoxSide.ABOVE));
    }


}
