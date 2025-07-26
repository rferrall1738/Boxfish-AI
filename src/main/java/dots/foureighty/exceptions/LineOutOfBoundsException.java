package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;

public class LineOutOfBoundsException extends BadLineException {
    public LineOutOfBoundsException( Line line) {
        super("Played line is out of bounds of the board", line);
    }
}
