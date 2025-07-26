package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;

public class LineAlreadyExistsException extends BadLineException {

    public LineAlreadyExistsException(Line line) {
        super("Move contains a line that has already been played",line);
    }

}
