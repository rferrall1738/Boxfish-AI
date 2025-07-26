package dots.foureighty.exceptions;

import dots.foureighty.lines.Line;

public class ExtraMoveException extends BadLineException {
    public ExtraMoveException(Line line) {
        super("An extra move was played when a box was not completed", line);
    }
}
