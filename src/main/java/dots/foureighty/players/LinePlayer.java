package dots.foureighty.players;

import dots.foureighty.gamebuilder.Game;

import java.awt.*;

public interface LinePlayer {
    Move getMove(Game gameState);
    String getName();
    Color getColor();
}
