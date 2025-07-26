package dots.foureighty.panels;

import dots.foureighty.gamebuilder.Board;
import dots.foureighty.gamebuilder.Game;
import dots.foureighty.lines.LineDirection;
import dots.foureighty.players.LinePlayer;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    protected final Game game;

    public GamePanel(Game game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoxes(g);
        drawPlayedLines(g);
        drawDots(g);
    }
    protected void drawPlayedLines(Graphics g){
        game.getMoves().forEach(moveBooleanPair -> {
            LinePlayer player = moveBooleanPair.getValue() ? game.getPlayer1() : game.getPlayer2();

            g.setColor(player.getColor());
            moveBooleanPair.getKey().getLines().forEach(line -> {
                g.fillRoundRect(getXPadding() + line.getX() * getCellDim(), getYPadding() + line.getY() * getCellDim(),
                        line.getDirection() == LineDirection.DOWN ? 10 : getCellDim() + 10,
                        line.getDirection() == LineDirection.DOWN ? getCellDim() + 10 : 10, 20, 20);
            });
        });
    }
    protected void drawDots(Graphics g){
        Board gameBoard = game.getGameBoard();
        g.setColor(Color.BLACK);
        for (int i = 0; i < gameBoard.getXSize(); i++) {
            for (int j = 0; j < gameBoard.getYSize(); j++) {
                g.fillOval(getXPadding() + i * getCellDim(), getYPadding() + j * getCellDim(), 10, 10);
            }
        }
    }
    protected void drawBoxes(Graphics g){
        //TODO: Draw these
    }

    protected int getYPadding() {
        return (getHeight() - (game.getGameBoard().getYSize() - 1) * getCellDim()) / 2;
    }

    protected int getXPadding() {
        return (getWidth() - (game.getGameBoard().getXSize() - 1) * getCellDim()) / 2;
    }

    protected int getSpaceSize() {
        return (int) (Math.min(getWidth(), getHeight()) * 0.75);
    }

    protected int getCellDim() {
        return getSpaceSize() / (Math.max(game.getGameBoard().getXSize(), game.getGameBoard().getYSize()) - 1);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}
