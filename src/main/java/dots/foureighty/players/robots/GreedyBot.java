package dots.foureighty.players.robots;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveBuilder;
import dots.foureighty.players.Player;

import java.awt.*;

public class GreedyBot extends Player {
    public GreedyBot() {
        super("Greedy Bot", Color.GREEN);
    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        MoveBuilder moveBuilder = new MoveBuilder(gameState.getBoard());
        while (!moveBuilder.isComplete()) {
            Line foundLine = searchForBox(moveBuilder.getCurrentBoard());
            if (foundLine == null) {
                foundLine = moveBuilder.getCurrentBoard().getUnplayedPositions().get(0);
            }
            moveBuilder.addLine(foundLine);
        }
        return moveBuilder.build();
    }

    /***
     * Searches for boxes in the board
     * @param board current baord state
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
