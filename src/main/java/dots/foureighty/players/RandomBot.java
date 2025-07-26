package dots.foureighty.players;

import dots.foureighty.gamebuilder.Board;
import dots.foureighty.gamebuilder.Game;
import dots.foureighty.lines.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/***
 * AI that only plays random moves.
 */
public class RandomBot implements LinePlayer {
    @Override
    public Move getMove(Game gameState) {

        Board board = gameState.getGameBoard();
        ArrayList<Line> move = new ArrayList<>();
        Random rand = new Random();
        boolean hasToMove = true;
        while (hasToMove) {
            Line lineToPlay = board.getValidLinePlacements().get(rand.nextInt(board.getValidLinePlacements().size()));
            move.add(lineToPlay);
            hasToMove = board.getCompletedBoxes(lineToPlay).size() > 0;
            board = board.addLine(lineToPlay);
        }
        return new Move(move);
    }

    @Override
    public String getName() {
        return "Random Bot";
    }

    @Override
    public Color getColor() {
        return Color.MAGENTA;
    }
}
