package dots.foureighty;

import dots.foureighty.moves.Move;

import java.util.Arrays;
import java.util.HashMap;

public class Game {

    private final Board board;
    private Player playerUpNow = Player.PLAYER1;
    private final HashMap<Player,Integer> scores = new HashMap<>();
    private GameListener listener;

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

    public void setPlayerPlacing(Player player) {
        playerUpNow = player;
    }

    public Board getBoard() {
        return board;
    }

    public void addGameListener(GameListener listener) {
        this.listener = listener;
    }
    
    public int getPlayerScore(Player player) {
        return scores.getOrDefault(player,0);
    }

    public void playMove(Move move) {

        Arrays.stream(move.getLines()).forEach(getBoard()::addLine);
    }

}
