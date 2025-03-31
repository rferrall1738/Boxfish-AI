package me.curtisbradley.moves;

import me.curtisbradley.Player;

public class Line {
    private final int x; // x of top left most dot
    private final int y; // y of top left most dot
    private final Direction direction;
    private final Player player;

    public Line(int x, int y, Direction direction, Player player) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public Player getPlayer() {
        return player;
    }

    public enum Direction {
        DOWN, RIGHT
    }
}
