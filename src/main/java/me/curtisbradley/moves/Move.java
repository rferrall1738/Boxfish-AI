package me.curtisbradley.moves;

import me.curtisbradley.Player;

public class Move {
    private final Line[] lines;
    private final Player player;
    public Move(Line[] lines, Player player) {
        this.lines = lines;
        this.player = player;
    }
    public Line[] getLines() {
        return lines;
    }
    public Player getPlayer() {
        return player;
    }

}
