package dots.foureighty.gamebuilder;

import dots.foureighty.players.LinePlayer;
import dots.foureighty.players.LocalHumanPlayer;

import java.awt.*;

public class GameFactory {
    private int xSize = 4;
    private int ySize = 4;
    private Board gameBoard;
    private LinePlayer player1;
    private LinePlayer player2;

    /***
     * GameFactory for creating new dots and boxes games
     */
    public GameFactory() {

    }

    /***
     * Override game factory with gameboard. Already placed lines will belong to nobody.
     * Overrides {@link GameFactory#setXSize(int)} and {@link GameFactory#setYSize(int)}.
     * @param board Already initiated gameboard.
     * @return GameFactory
     * @see GameFactory#setXSize(int)
     * @see GameFactory#setYSize(int)
     */
    public GameFactory setBoard(Board board){
        this.gameBoard = board;
        return this;
    }

    /***
     * Sets the number of horizontal dots
     * @param xSize Number of horizontal dots
     * @return The GameFactory
     */
    public GameFactory setXSize(int xSize){
        this.xSize = xSize;
        return this;
    }
    /***
     * Sets the number of vertical dots
     * @param ySize Number of vertical dots
     * @return The GameFactory
     */
    public GameFactory setYSize(int ySize){
        this.ySize = ySize;
        return this;
    }
    /***
     * Sets Player1
     * @param player1 The first player.
     * @return The GameFactory
     * @see LinePlayer
     */
    public GameFactory setPlayer1(LinePlayer player1){
        this.player1 = player1;
        return this;
    }

    /***
     * Sets Player2
     * @param player2 The second player.
     * @return The GameFactory
     * @see LinePlayer
     */
    public GameFactory setPlayer2(LinePlayer player2){
        this.player2 = player2;
        return this;
    }

    private int getXSize(){
        return gameBoard == null ? xSize : gameBoard.getXSize();
    }
    private int getYSize(){
        return gameBoard == null ? ySize : gameBoard.getYSize();
    }

    /***
     * Builds a game object from the GameFactory.
     * @return Build game object.
     */
    public Game build() {
        long numberOfPossibleLines = 2L * getXSize() * getYSize() - getXSize() - getYSize();

        if (numberOfPossibleLines < 0 || numberOfPossibleLines > Integer.MAX_VALUE) {
            throw new RuntimeException("Unsupported Board Size");
        }
        if (this.player1 == null) {
            Color player1Color = Color.RED;
            if (this.player2 != null) {
                player1Color = invertColor(player1Color);
            }
            this.player1 = new LocalHumanPlayer("Player 1", player1Color);
        }
        if (this.player2 == null ){
            this.player2 = new LocalHumanPlayer("Player 2", invertColor(this.player1.getColor()));
        }
        if (gameBoard == null) {
            gameBoard = new Board(xSize, ySize);
        }
        return new Game(gameBoard,player1,player2);
    }

    private Color invertColor(Color color){
        return  new Color(~color.getRGB() ^ 0x07);
    }

}
