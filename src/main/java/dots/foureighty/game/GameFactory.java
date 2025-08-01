package dots.foureighty.game;

import dots.foureighty.game.boards.Board;
import dots.foureighty.game.boards.BoardGenerator;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.listeners.GameUpdateListener;
import dots.foureighty.players.LocalHumanPlayer;
import dots.foureighty.players.Player;
import dots.foureighty.util.ColorUtils;

import java.awt.*;
import java.util.ArrayList;

public class GameFactory {
    private int xSize = 4;
    private int ySize = 4;
    private Board gameBoard;
    private Player player1;
    private Player player2;
    private Color player1Color;
    private Color player2Color;
    private BoardGenerator boardGenerator = StandardBoards.AMERICAN;
    private final ArrayList<GameUpdateListener> updateListeners = new ArrayList<>();

    /***
     * GameFactory for creating new dots and boxes games
     */
    public GameFactory() {
    }

    /***
     * Override game factory with gameboard. Already placed lines will belong to nobody.
     * Overrides {@link GameFactory#withXSize(int)}, {@link GameFactory#withYSize(int)},
     * and {@link GameFactory#withBoardGenerator(BoardGenerator)}
     * @param board Already initiated gameboard.
     * @return GameFactory
     * @see GameFactory#withXSize(int)
     * @see GameFactory#withYSize(int)
     * @see GameFactory#withBoardGenerator(BoardGenerator)
     */
    public GameFactory withBoard(Board board) {
        this.gameBoard = board;
        return this;
    }

    /***
     * Sets the number of horizontal dots
     * @param xSize Number of horizontal dots
     * @return The GameFactory
     */
    public GameFactory withXSize(int xSize) {
        this.xSize = xSize;
        return this;
    }

    /***
     * Sets the number of vertical dots
     * @param ySize Number of vertical dots
     * @return The GameFactory
     */
    public GameFactory withYSize(int ySize) {
        this.ySize = ySize;
        return this;
    }

    /***
     * Sets Player1
     * @param player1 The first player.
     * @return The GameFactory
     * @see Player
     */
    public GameFactory withPlayer1(Player player1) {
        this.player1 = player1;
        return this;
    }

    /***
     * Sets Player2
     * @param player2 The second player.
     * @return The GameFactory
     * @see Player
     */
    public GameFactory withPlayer2(Player player2) {
        this.player2 = player2;
        return this;
    }

    private int getXSize() {
        return gameBoard == null ? xSize : gameBoard.getXSize();
    }

    private int getYSize() {
        return gameBoard == null ? ySize : gameBoard.getYSize();
    }

    /***
     * Registers a method that will be run when a move is made. Listeners are ran on new threads.
     * @param listener the listener to register
     * @return The game factory
     */
    public GameFactory withUpdateListener(GameUpdateListener listener) {
        this.updateListeners.add(listener);
        return this;
    }

    /***
     * Sets the color of player 1's lines and boxes.
     * @param player1Color color for player 1
     * @return The Game Factory
     */
    public GameFactory withPlayer1Color(Color player1Color) {
        this.player1Color = player1Color;
        return this;
    }

    /***
     * Sets the color of player 2's lines and boxes.
     * @param player2Color color for player 2
     * @return The Game Factory
     */
    public GameFactory withPlayer2Color(Color player2Color) {
        this.player2Color = player2Color;
        return this;
    }

    /***
     * Sets the generator for creating the board
     * @param boardGenerator The board generator
     * @return The game factory
     * @see BoardGenerator
     * @see StandardBoards
     */
    public GameFactory withBoardGenerator(BoardGenerator boardGenerator) {
        this.boardGenerator = boardGenerator;
        return this;
    }

    /***
     * Builds a game object from the GameFactory.
     * @return Build game object.
     */
    public Game build() {
        if (this.player1 == null) {
            this.player1 = new LocalHumanPlayer("Player 1", player1Color);
        }
        if (this.player2 == null) {
            this.player2 = new LocalHumanPlayer("Player 2", player2Color);
        }

        if (player1Color != null) {
            player1.setColor(player1Color);
        }
        if (player2Color != null) {
            player2.setColor(player2Color);
        }


        if (player1.getColor() == null && player2.getColor() == null) {
            player1.setColor(Color.RED);
            player2.setColor(Color.BLUE);
        } else {
            if (player1.getColor() == null || player2.getColor() == null) {
                if (player1.getColor() != null) {
                    player2.setColor(ColorUtils.invertColor(player1.getColor()));
                } else {
                    player1.setColor(ColorUtils.invertColor(player2.getColor()));
                }
            }
        }

        // If the colors are similar to each other, make player2 use the inverse of player1's color
        if (ColorUtils.computeColorSimilarity(player1.getColor(), player2.getColor()) < 0.2) {
            if (ColorUtils.computeColorSimilarity(player1.getColor(), ColorUtils.invertColor(player2.getColor())) > 0.2) {
                player2.setColor(ColorUtils.invertColor(player1.getColor()));
            }
        }

        if (gameBoard == null) {
            gameBoard = boardGenerator.generateBoard(getXSize(), getYSize());
        }
        Game game = new Game(gameBoard, player1, player2);

        updateListeners.forEach(game::registerGameUpdateListener);

        return game;
    }



}
