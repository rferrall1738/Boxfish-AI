import dots.foureighty.game.boards.Board;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.lines.MoveIterator;

public class MoveIteratorTest {
    public void runTests() {
        Board board = StandardBoards.AMERICAN.generateBoard(5, 5);
        MoveIterator iterator = new MoveIterator(board);
        int length = 0;
        while (iterator.hasNext()) {
            length++;
            iterator.next();
        }
        assert length == 40;
    }
}
