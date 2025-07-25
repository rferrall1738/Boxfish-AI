package dots.foureighty.players;

import dots.foureighty.gamebuilder.NewGame;

import java.awt.*;

public interface LinePlayer {
    Move getMove(NewGame gameState);
    String getName();
    Color getColor();
}
