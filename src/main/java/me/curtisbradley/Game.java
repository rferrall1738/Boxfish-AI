package me.curtisbradley;

import java.util.HashMap;

public class Game {

    private final Board board;
    private Player playerUpNow = Player.PLAYER1;
    private final HashMap<Player,Integer> scores = new HashMap<>();

    public Game(int xSize, int ySize) {
        board = new Board(xSize, ySize);
    }

    public int getXSize() {
        return board.getXSize();
    }

    public int getYSize() {
        return board.getYSize();
    }

    public Player getPlayerPlacing() {
        return playerUpNow;
    }

    public Board getBoard() {
        return board;
    }

    public int getPlayerScore(Player player) {
        return scores.getOrDefault(player,0);
    }



}
