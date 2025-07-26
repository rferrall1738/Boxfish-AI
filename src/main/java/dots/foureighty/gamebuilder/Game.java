package dots.foureighty.gamebuilder;

import dots.foureighty.players.LinePlayer;
import dots.foureighty.players.Move;
import javafx.util.Pair;

import java.util.ArrayList;

public class Game {
    private Board gameBoard;
    private final LinePlayer player1;
    private final LinePlayer player2;

    private final ArrayList<Pair<Move,Boolean>> moves = new  ArrayList<>();

    private boolean isPlayer1Turn = true;

    protected Game(Board board, LinePlayer player1, LinePlayer player2) {
        this.gameBoard = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    public LinePlayer getPlayer1() {
        return player1;
    }
    public LinePlayer getPlayer2() {
        return player2;
    }
    public boolean isPlayer1Turn() {
        return this.isPlayer1Turn;
    }

    /***
     * Checks if there are any moves left to play
     * @return True if there are no more moves.
     */
    public boolean hasEnded(){
        return gameBoard.getValidLinePlacements().size() == 0;
    }

    /***
     * Get the gameboard
     * @return The game board
     */
    public Board getGameBoard(){
        return this.gameBoard;
    }

    /***
     * Get the played moves
     * @return A list of tuples, the first contains the move, the second value is if it was played by player 1.
     */
    public ArrayList<Pair<Move,Boolean>> getMoves(){
        return this.moves;
    }
    public void play() {
        while (!hasEnded()) {
            LinePlayer currentPlayer = isPlayer1Turn ? player1 : player2;
            Move playedMove = currentPlayer.getMove(this);
            this.gameBoard = playedMove.validate(gameBoard); // Will throw an exception if the move is bad

            moves.add(new Pair<>(playedMove,isPlayer1Turn()));
            isPlayer1Turn = !isPlayer1Turn;
        }

    }




}
