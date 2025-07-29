package dots.foureighty.game;

import dots.foureighty.exceptions.*;
import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.PlayedMove;
import dots.foureighty.listeners.GameUpdateListener;
import dots.foureighty.listeners.GameUpdateType;
import dots.foureighty.players.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Game {
    private Board gameBoard;
    private final Player player1;
    private final Player player2;
    private final ArrayList<Point> player1Boxes = new ArrayList<>();
    private final ArrayList<Point> player2Boxes = new ArrayList<>();
    private final ArrayList<GameUpdateListener> gameUpdateListeners = new ArrayList<>();

    private final ArrayList<PlayedMove> moves = new ArrayList<>();

    private boolean isPlayer1Turn = true;

    Game(Board board, Player player1, Player player2) {
        this.gameBoard = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isPlayer1Turn() {
        return this.isPlayer1Turn;
    }

    /***
     * Checks if there are any moves left to play
     * @return True if there are no more moves.
     */
    public boolean isActive() {
        return !gameBoard.getUnplayedPositions().isEmpty();
    }

    /***
     * Get the gameboard
     * @return The game board
     */
    public Board getGameBoard() {
        return this.gameBoard;
    }

    /***
     * Get the played moves
     * @return A list of tuples, the first contains the move, the second value is if it was played by player 1.
     */
    public ArrayList<PlayedMove> getMoves() {
        return (ArrayList<PlayedMove>) this.moves.clone();
    }

    public void play() {
        new Thread(() -> {
            notifyUpdateListeners(GameUpdateType.GAME_START);
            while (isActive()) {
                Player currentPlayer = isPlayer1Turn() ? player1 : player2;
                Move playedMove = currentPlayer.getMove(this.generateSnapshot());
                playMove(playedMove);
                moves.add(new PlayedMove(playedMove.getLines(), isPlayer1Turn()));

                isPlayer1Turn = !isPlayer1Turn;
                notifyUpdateListeners(GameUpdateType.MOVE_PLAYED);
            }
            if (gameBoard.getUnplayedPositions().isEmpty()) {
                notifyUpdateListeners(GameUpdateType.GAME_END);
            }
        }).start();
    }

    private void playMove(Move move) throws InvalidMoveException {
        if (move.getLines().isEmpty()) {
            throw new EmptyMoveException();
        }
        Board newBoard = getGameBoard();
        boolean hasMove = true;
        ArrayList<Point> boxes = isPlayer1Turn() ? player1Boxes : player2Boxes;

        for (Line line : move.getLines()) {
            if (!hasMove) {
                throw new ExtraMoveException(line);
            }
            if (newBoard.getIndexFromLine(line) == -1) {
                throw new LineOutOfBoundsException(line);
            }
            if (newBoard.containsLine(line)) {
                throw new LineAlreadyExistsException(line);
            }
            ArrayList<Point> newBoxes = newBoard.getCompletedBoxLocations(line);
            boxes.addAll(newBoxes);

            hasMove = !newBoxes.isEmpty() && newBoard.getUnplayedPositions().size() > 1;
            newBoard = newBoard.append(line);
        }
        if (hasMove) {
            throw new MissingExtraMoveException();
        }

        this.gameBoard = newBoard;
    }

    public GameSnapshot generateSnapshot() {
        return new GameSnapshot(getGameBoard().clone(), getPlayer1().getName(), getPlayer2().getName(), getPlayer1().getColor(), getPlayer2().getColor(),
                getPlayer1Boxes().toArray(new Point[0]), getPlayer2Boxes().toArray(new Point[0]),
                getMoves().toArray(new PlayedMove[0]), isPlayer1Turn(), !isActive());
    }

    public ArrayList<Point> getPlayer1Boxes() {
        return (ArrayList<Point>) player1Boxes.clone();
    }

    public ArrayList<Point> getPlayer2Boxes() {
        return (ArrayList<Point>) player2Boxes.clone();
    }

    public void registerGameUpdateListener(GameUpdateListener gameUpdateListener) {
        this.gameUpdateListeners.add(gameUpdateListener);
    }

    public void unregisterGameUpdateListener(GameUpdateListener gameUpdateListener) {
        this.gameUpdateListeners.remove(gameUpdateListener);
    }

    private void notifyUpdateListeners(GameUpdateType updateType) {
        gameUpdateListeners.stream().filter(GameUpdateListener::unregister).collect(Collectors.toList()).forEach(this::unregisterGameUpdateListener);
        gameUpdateListeners.forEach((gameUpdateListener) -> new Thread(() -> gameUpdateListener.handleUpdate(generateSnapshot(), updateType)).start());

    }

}
