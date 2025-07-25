package dots.foureighty.players;

import dots.foureighty.Main;
import dots.foureighty.gamebuilder.NewGame;

import java.awt.*;

public class LocalHumanPlayer implements LinePlayer {
    private String playerName;
    private Color playerColor;

    public LocalHumanPlayer(String playerName, Color playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;

    }
    @Override
    public Move getMove(NewGame gameState) {
        //TODO: Change as needed.

        return null;
    }

    @Override
    public String getName() {
        return playerName;
    }

    @Override
    public Color getColor() {
        return playerColor;
    }
}
