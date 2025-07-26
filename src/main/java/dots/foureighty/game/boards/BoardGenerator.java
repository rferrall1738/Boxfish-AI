package dots.foureighty.game.boards;

import dots.foureighty.exceptions.UnsupportedBoardSizeException;

public abstract class BoardGenerator {
    public Board generateBoard(int width, int height) {
        if (width <= 2 || height <= 2) {
            throw new UnsupportedBoardSizeException("Cannot have a dimension of less than 2 dots");
        }
        if (width > 0xff || height > 0xff) {
            throw new UnsupportedBoardSizeException("Dimensions cannot be greater than 0xff");
        }
        return new Board(width, height);
    }
}
