package dots.foureighty.game.boards;

import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;

/***
 * Some starting positions are known by different names. This class contains generators for common starting positions.
 */
public final class StandardBoards {
    private StandardBoards() {
    }

    /***
     * Standard grid. No starting lines.
     */
    public static final BoardGenerator AMERICAN = new BoardGenerator() {
        @Override
        public Board generateBoard(int width, int height) {
            return super.generateBoard(width, height);
        }
    };

    /***
     * The left and bottom borders start filled in.
     */
    public static final BoardGenerator ICELANDIC = new BoardGenerator() {
        @Override
        public Board generateBoard(int width, int height) {
            Board board = AMERICAN.generateBoard(width, height);
            //Add lines on left
            for (int i = 0; i < width - 1; i++) {
                board = board.append(new Line(0, i, LineDirection.DOWN));
            }
            //add lines on bottom
            for (int i = 0; i < height - 1; i++) {
                board = board.append(new Line(i, height - 1, LineDirection.RIGHT));
            }
            return board;
        }
    };
    /***
     * The outside borders of the grid start filled in
     */
    public static final BoardGenerator SWEDISH = new BoardGenerator() {
        @Override
        public Board generateBoard(int width, int height) {
            Board board = ICELANDIC.generateBoard(width, height); // add lines to left and bottom.
            for (int i = 0; i < width - 1; i++) {
                board = board.append(new Line(i, 0, LineDirection.RIGHT));
            }
            //Add lines on right
            for (int i = 0; i < height - 1; i++) {
                board = board.append(new Line(width - 1, i, LineDirection.DOWN));
            }
            return board;
        }
    };


}
