package dots.foureighty.game;

import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Move;
import javafx.util.Pair;

import java.awt.*;

/***
 * Snapshot of the current game state
 */
public class GameSnapshot {
    private final Board board;
    private final String player1Name;
    private final String player2Name;
    private final Color player1Color;
    private final Color player2Color;
    private final Point[] player1Boxes;
    private final Point[] player2Boxes;
    private final Pair<Move, Boolean>[] playedMoves;
    private final boolean isPlayer1Turn;
    private final boolean hasEnded;

    GameSnapshot(Board board, String player1Name, String player2Name, Color player1Color, Color player2Color, Point[] player1Boxes, Point[] player2Boxes, Pair<Move, Boolean>[] playedMoves, boolean isPlayer1Turn, boolean hasEnded) {
        this.board = board;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        this.player1Boxes = player1Boxes;
        this.player2Boxes = player2Boxes;
        this.playedMoves = playedMoves;
        this.isPlayer1Turn = isPlayer1Turn;
        this.hasEnded = hasEnded;
    }

    public Board getBoard() {
        return board;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public Color getPlayer1Color() {
        return player1Color;
    }

    public Color getPlayer2Color() {
        return player2Color;
    }

    public Point[] getPlayer1Boxes() {
        return player1Boxes;
    }

    public Point[] getPlayer2Boxes() {
        return player2Boxes;
    }

    public Pair<Move, Boolean>[] getPlayedMoves() {
        return playedMoves;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public boolean hasEnded() {
        return hasEnded;
    }
}
