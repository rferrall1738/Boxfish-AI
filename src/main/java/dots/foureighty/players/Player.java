package dots.foureighty.players;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;

import java.awt.*;

public abstract class Player {
    private Color playerColor;
    private String playerName;

    public Player(String name, Color color) {
        this.playerName = name;
        this.playerColor = color;
    }

    public Color getColor() {
        return playerColor;
    }

    public void setColor(Color color) {
        this.playerColor = color == null ? color : new Color(color.getRGB() | 0x07);
    }

    public String getName() {
        return playerName;
    }

    public void setName(String name) {
        this.playerName = name;
    }

    public abstract Move getMove(GameSnapshot gameState);


}
