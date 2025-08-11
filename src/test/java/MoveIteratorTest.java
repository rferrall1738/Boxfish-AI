import dots.foureighty.game.boards.Board;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.lines.MoveIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoveIteratorTest {
    @Test
    public void runTests() {
        Board board = StandardBoards.AMERICAN.generateBoard(5, 5);
        MoveIterator iterator = new MoveIterator(board);
        int length = 0;
        while (iterator.hasNext()) {
            length++;
            iterator.next();
        }
        assertEquals(40, length);
    }
}
