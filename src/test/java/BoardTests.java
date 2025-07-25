import dots.foureighty.gamebuilder.Board;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;

public class BoardTests {
    public void runTests(){
        badBoardSize();
        boardInit();
        checkLineConversions();
        checkLineToIndex();
        checkLineEqualities();
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
    }
    public void checkLineToIndex() {
        Board board = new Board(3,3);
        assert board.getIndexOfLine(new Line(0,0, LineDirection.DOWN)) == 0;
        assert board.getIndexOfLine(new Line(0,0, LineDirection.RIGHT)) == 1;
        assert board.getIndexOfLine(new Line(1,0, LineDirection.RIGHT)) == 3;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.RIGHT)) == -1;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.DOWN)) == 5;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.RIGHT)) == -1;
        assert board.getIndexOfLine(new Line(1,1, LineDirection.DOWN)) == 8;
        assert board.getIndexOfLine(new Line(2,0, LineDirection.RIGHT)) == -1;
        assert board.getIndexOfLine(new Line(1,2, LineDirection.RIGHT)) == 11;
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
    public void checkLineConversions() {
        Board board = new Board(3,3);
        Line topLeftDown = new Line(0,0,LineDirection.DOWN);
        Line topRightDown = new Line(2,1,LineDirection.DOWN);
        Line bottomLeftRight = new Line(1,2,LineDirection.DOWN);

        assert board.convertIndexToLine(board.getIndexOfLine(topLeftDown)).equals(topLeftDown);
        assert board.convertIndexToLine(board.getIndexOfLine(topRightDown)).equals(topRightDown);
        assert board.convertIndexToLine(board.getIndexOfLine(bottomLeftRight)).equals(bottomLeftRight);
        assert board.convertIndexToLine(board.getIndexOfLine(bottomLeftRight)).equals(bottomLeftRight);
    }

}
