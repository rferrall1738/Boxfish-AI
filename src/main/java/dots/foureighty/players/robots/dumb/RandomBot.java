package dots.foureighty.players.robots.dumb;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveBuilder;
import dots.foureighty.players.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/***
 * AI that only plays random moves.
 */
public class RandomBot implements Player {
    private final static Random RANDOM = new Random();

    private Color color = Color.MAGENTA;


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
        return "RandomBot";
    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        return randomMove(gameState.getBoard());
    }

    public Move randomMove(Board board) {
        MoveBuilder moveBuilder = new MoveBuilder(board);
        while (!moveBuilder.isComplete()) {
            ArrayList<Line> possibleLines = moveBuilder.getCurrentBoard().getUnplayedPositions();
            moveBuilder.addLine(possibleLines.get(RANDOM.nextInt(possibleLines.size())));
        }
        return moveBuilder.build();
    }

}
