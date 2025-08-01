package dots.foureighty.players.robots.dumb;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveBuilder;
import dots.foureighty.players.Player;

import java.awt.*;

public class GreedyBot implements Player {
    private Color color = Color.GREEN;
    private final RandomBot randomBot = new RandomBot();
    public GreedyBot() {
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return "GreedyBot";
    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        return getMaximizingMove(gameState.getBoard());
    }

    public Move getMaximizingMove(Board board) {
        MoveBuilder moveBuilder = new MoveBuilder(board);
        while (!moveBuilder.isComplete()) {
            Line foundLine = searchForBox(moveBuilder.getCurrentBoard());
            if (foundLine == null) {
                //Play a random line if there is nothing to take.
                moveBuilder.addAll(randomBot.randomMove(moveBuilder.getCurrentBoard()).getLines());
            } else {
                moveBuilder.addLine(foundLine);
            }
        }
        return moveBuilder.build();
    }

    /***
     * Searches for boxes in the board
     * @param board current board state
     * @return The first box it finds. Null if none is found.
     */
    private Line searchForBox(Board board) {
        for (Line line : board.getUnplayedPositions()) {
            if (board.completesBox(line)) {
                return line;
            }
        }
        return null;
    }
}
