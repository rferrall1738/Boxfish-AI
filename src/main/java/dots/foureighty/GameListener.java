package dots.foureighty;

import dots.foureighty.lines.PlayedLine;

public interface GameListener {
    void onMoveMade(Game game, PlayedLine playedLine);
}
