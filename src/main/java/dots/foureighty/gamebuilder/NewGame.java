package dots.foureighty.gamebuilder;

import dots.foureighty.players.LinePlayer;
import dots.foureighty.players.Move;
import javafx.util.Pair;

import java.util.ArrayList;

public class NewGame {
    private final Board gameBoard;
    private final LinePlayer player1;
    private final LinePlayer player2;

    //All the moves that have been made, the boolean is if the move was made by player1
    private ArrayList<Pair<Move,Boolean>> moves = new  ArrayList<>();

    private boolean isPlayer1Turn = true;

    protected NewGame(Board board, LinePlayer player1, LinePlayer player2) {
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

    public boolean hasEnded(){
        //todo: Add logic
        return false;
    }

    public Board getGameBoard(){
        return this.gameBoard;
    }

    public ArrayList<Pair<Move,Boolean>> getMoves(){
        return this.moves;
    }
    public void play() {
        while (!hasEnded()) {
            LinePlayer currentPlayer = isPlayer1Turn ? player1 : player2;
            Move playedMove = currentPlayer.getMove(this);

            playedMove.validate(gameBoard); // Will throw an exception if the move is bad

            moves.add(new Pair<>(playedMove,isPlayer1Turn()));
            isPlayer1Turn = !isPlayer1Turn;
        }

    }




}
