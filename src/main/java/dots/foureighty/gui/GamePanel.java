package dots.foureighty.gui;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;
import dots.foureighty.lines.PlayedMove;

import javax.swing.*;
import java.awt.*;
import java.util.BitSet;

public class GamePanel extends JPanel {
    protected GameSnapshot game;
    private static final Color NON_PLAYER_LINES = Color.BLACK;
    private static final float PLAYED_BOX_ALPHA = 0.5f;
    protected static final int LINE_WIDTH = 10;

    public GamePanel(GameSnapshot game) {
        this.game = game;
    }

    public void updateGame(GameSnapshot newSnapShot) {
        this.game = newSnapShot;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoxes(g);
        drawLines(g);
        drawDots(g);
    }

    private void drawLines(Graphics g) {
        Board board = StandardBoards.AMERICAN.generateBoard(game.getBoard().getXSize(), game.getBoard().getYSize());
        for (PlayedMove move : game.getPlayedMoves()) { //Draw the player-made lines
            Color playerColor = move.isFromPlayerOne() ? game.getPlayer1Color() : game.getPlayer2Color();
            g.setColor(playerColor);
            for (Line line : move.getLines()) {
                board = board.append(line);
                drawLine(g, line);
            }
        }

        //Anything that is on the board and wasn't played by a player (starting conditions) should also be drawn.
        BitSet remainingMoves = game.getBoard().getLineBitSet();
        remainingMoves.andNot(board.getLineBitSet());

        g.setColor(NON_PLAYER_LINES);
        final Board referenceBoard = board;
        remainingMoves.stream().forEach((lineIndex) -> drawLine(g, referenceBoard.getLineFromIndex(lineIndex)));


    }

    protected void drawLine(Graphics g, Line line) {
        g.fillRoundRect(getXPadding() + line.getX() * getCellDim(), getYPadding() + line.getY() * getCellDim(),
                line.getDirection() == LineDirection.DOWN ? LINE_WIDTH : getCellDim() + LINE_WIDTH,
                line.getDirection() == LineDirection.DOWN ? getCellDim() + LINE_WIDTH : LINE_WIDTH, LINE_WIDTH * 2, LINE_WIDTH * 2);
    }

    protected void drawDots(Graphics g) {
        Board gameBoard = game.getBoard();
        g.setColor(Color.BLACK);
        for (int i = 0; i < gameBoard.getXSize(); i++) {
            for (int j = 0; j < gameBoard.getYSize(); j++) {
                g.fillOval(getXPadding() + i * getCellDim(), getYPadding() + j * getCellDim(), LINE_WIDTH, LINE_WIDTH);
            }
        }
    }

    private void drawBoxes(Graphics g) {

        g.setColor(setAlpha(game.getPlayer1Color(), PLAYED_BOX_ALPHA).darker());

        for (Point p : game.getPlayer1Boxes()) {
            drawBox(g, p);
        }

        g.setColor(setAlpha(game.getPlayer2Color(), PLAYED_BOX_ALPHA).darker());

        for (Point p : game.getPlayer2Boxes()) {
            drawBox(g, p);
        }
    }

    protected Color setAlpha(Color color, float alpha) {
        float[] rgb = new float[4];
        color.getRGBComponents(rgb);
        rgb[3] = alpha;
        return new Color(rgb[0], rgb[1], rgb[2], rgb[3]);
    }

    protected void drawBox(Graphics g, Point cornerOfBox) {
        g.fillRect(LINE_WIDTH + getXPadding() + (int) cornerOfBox.getX() * getCellDim(),
                LINE_WIDTH + getYPadding() + (int) cornerOfBox.getY() * getCellDim(),
                getCellDim() - LINE_WIDTH, getCellDim() - LINE_WIDTH);
    }

    protected int getYPadding() {
        return (getHeight() - (game.getBoard().getYSize() - 1) * getCellDim()) / 2;
    }

    protected int getXPadding() {
        return (getWidth() - (game.getBoard().getXSize() - 1) * getCellDim()) / 2;
    }

    protected int getSpaceSize() {
        return (int) (Math.min(getWidth(), getHeight()) * 0.75);
    }

    protected int getCellDim() {
        return getSpaceSize() / (Math.max(game.getBoard().getXSize(), game.getBoard().getYSize()) - 1);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}
