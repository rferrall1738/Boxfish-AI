import dots.foureighty.gamebuilder.Board;
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
        checkValidLines();
    }

    public void badBoardSize(){
        try {
            Board board = new Board(-1,-1);
            throw new AssertionError("Invalid board did not error");
        } catch(IllegalArgumentException ignore) {}
        catch(Exception other) {
            throw new AssertionError("Unexpected exception", other);
        }
    }
    public void boardInit() {
        try {
            Board board = new Board(1,1);
        } catch(Exception error) {
            throw new AssertionError("Unexpected exception", error);
        }

        Board dummyBoard = new Board(2,2, BitSet.valueOf(new byte[]{0b1101}));
        assert dummyBoard.isBitSetValid();
        Board dummyBoard2 = new Board(3,3, BitSet.valueOf(new byte[]{0b1101}));
        assert !dummyBoard2.isBitSetValid();
        Board dummyBoard3 = new Board(3,3, BitSet.valueOf(new byte[]{0b1101101,0b01001}));
        assert dummyBoard3.isBitSetValid();
    }
    public void checkContainsLine() {
        Board dummyBoard = new Board(2,2, BitSet.valueOf(new byte[]{0b1011}));
        assert dummyBoard.isBitSetValid();

        assert dummyBoard.getIndexOfLine(new Line(0,0, LineDirection.DOWN)) == 0;

        assert dummyBoard.containsLine(new Line(0,0, LineDirection.DOWN));
        assert dummyBoard.containsLine(new Line(0,0, LineDirection.RIGHT));
        assert !dummyBoard.containsLine(new Line(1,0, LineDirection.DOWN));
        assert dummyBoard.containsLine(new Line(0,1, LineDirection.RIGHT));


        assert !dummyBoard.containsLine(new Line(1,0, LineDirection.RIGHT));
        assert !dummyBoard.containsLine(new Line(1,1, LineDirection.RIGHT));
        assert !dummyBoard.containsLine(new Line(1,1, LineDirection.DOWN));
        assert !dummyBoard.containsLine(new Line(0,1, LineDirection.DOWN));

        Board dummyBoard3 = new Board(3,3, BitSet.valueOf(new byte[]{(byte) 0b10111111, (byte) 0b1110}));

        assert dummyBoard3.isBitSetValid();

        assert dummyBoard3.containsLine(new Line(1,2, LineDirection.RIGHT));
        assert !dummyBoard3.containsLine(new Line(0,1, LineDirection.RIGHT));
        assert dummyBoard3.containsLine(new Line(2,0, LineDirection.DOWN));
    }
    public void checkLineToIndex() {
        Board board = new Board(3,3);
        assert board.getIndexOfLine(new Line(0,0, LineDirection.DOWN)) == 0;
        assert board.getIndexOfLine(new Line(0,0, LineDirection.RIGHT)) == 1;
        assert board.getIndexOfLine(new Line(1,0, LineDirection.RIGHT)) == 3;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.RIGHT)) == -1;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.DOWN)) == 4;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.RIGHT)) == -1;
        assert board.getIndexOfLine(new Line(1,1, LineDirection.DOWN)) == 7;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.RIGHT)) == -1;
        assert board.getIndexOfLine(new Line(1,2, LineDirection.RIGHT)) == 11;
        assert board.getIndexOfLine(new Line(2,2, LineDirection.RIGHT)) == -1;
    }
    public void checkIndexToLine() {
        Board board = new Board(3,3);
        assert board.getIndexFromLine(0).equals(new Line(0,0, LineDirection.DOWN)) ;
        assert board.getIndexFromLine(1).equals(new Line(0,0, LineDirection.RIGHT)) ;
        assert board.getIndexFromLine(3).equals(new Line(1,0, LineDirection.RIGHT)) ;
        assert board.getIndexFromLine(4).equals(new Line(2,0, LineDirection.DOWN)) ;
        assert board.getIndexFromLine(7).equals(new Line(1,1, LineDirection.DOWN)) ;
        assert board.getIndexFromLine(11).equals(new Line(1,2, LineDirection.RIGHT)) ;
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
        Board board = new Board(3,3);
        Line topLeftDown = new Line(0,0,LineDirection.DOWN);
        Line topRightDown = new Line(2,1,LineDirection.DOWN);
        Line bottomLeftRight = new Line(1,2,LineDirection.DOWN);

        assert board.getIndexFromLine(board.getIndexOfLine(topLeftDown)).equals(topLeftDown);
        assert board.getIndexFromLine(board.getIndexOfLine(topRightDown)).equals(topRightDown);
        assert board.getIndexFromLine(board.getIndexOfLine(bottomLeftRight)) == null;

        Board bigBoard = new Board(5,5);
        assert bigBoard.getIndexFromLine(39).equals(new Line(3,4,LineDirection.RIGHT));
    }
    public void checkBoxCompletes(){
        Board dummyBoard = new Board(2,2, BitSet.valueOf(new byte[]{0b1011}));
        assert dummyBoard.getCompletedBoxes(new Line(1,0, LineDirection.DOWN)).equals(EnumSet.of(BoxSide.LEFT));


        Board dummyBoard2 = new Board(3,3, BitSet.valueOf(new byte[]{(byte) 0b01100010,0b1111}));
        assert dummyBoard2.isBitSetValid();
        assert dummyBoard2.getCompletedBoxes(new Line(0,1, LineDirection.DOWN)).equals(EnumSet.noneOf(BoxSide.class));
        assert dummyBoard2.getCompletedBoxes(new Line(1,1, LineDirection.DOWN)).equals(EnumSet.of(BoxSide.LEFT,BoxSide.RIGHT));
        Board dummyBoard3 = new Board(2,3, BitSet.valueOf(new byte[]{0b1101111}));
        assert dummyBoard3.isBitSetValid();

        assert dummyBoard3.getCompletedBoxes(new Line(0,1, LineDirection.RIGHT)).equals(EnumSet.of(BoxSide.ABOVE, BoxSide.BELOW));
    }

    public void checkValidLines(){
        Board bigBoard = new Board(5,5);
        assert bigBoard.getIndexOfLine(new Line(4,1,LineDirection.RIGHT)) == -1;
        assert !bigBoard.getValidLinePlacements().contains(new Line(4,1,LineDirection.RIGHT));
        assert bigBoard.getValidLinePlacements().contains(new Line(4,1,LineDirection.DOWN));
        assert !bigBoard.getValidLinePlacements().contains(new Line(4,4,LineDirection.DOWN));
        assert !bigBoard.getValidLinePlacements().contains(new Line(4,4,LineDirection.RIGHT));
        assert bigBoard.getValidLinePlacements().contains(new Line(3,4,LineDirection.RIGHT));
    }
}
