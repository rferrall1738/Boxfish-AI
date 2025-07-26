package dots.foureighty.players.robots;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.Move;
import dots.foureighty.players.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/***
 * AI that only plays random moves.
 */
public class RandomBot extends Player {

    public RandomBot() {
        super("Random Bot", Color.MAGENTA);
    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        Board board = gameState.getBoard();
        ArrayList<Line> move = new ArrayList<>();
        Random rand = new Random();
        boolean hasToMove = true;
        while (hasToMove && !board.getUnplayedPositions().isEmpty()) {
            Line lineToPlay = board.getUnplayedPositions().get(rand.nextInt(board.getUnplayedPositions().size()));
            move.add(lineToPlay);
            hasToMove = !board.getCompletedBoxes(lineToPlay).isEmpty();
            board = board.append(lineToPlay);
        }
        return new Move(move);
    }

}
