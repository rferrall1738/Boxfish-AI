package dots.foureighty.players.robots;

import dots.foureighty.game.GameSnapshot;
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
public class RandomBot extends Player {
    private final static Random RANDOM = new Random();


    public RandomBot() {
        super("Random Bot", Color.MAGENTA);
    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        MoveBuilder moveBuilder = new MoveBuilder(gameState.getBoard());
        while (!moveBuilder.isComplete()) {
            ArrayList<Line> possibleLines = moveBuilder.getCurrentBoard().getUnplayedPositions();
            moveBuilder.addLine(possibleLines.get(RANDOM.nextInt(possibleLines.size())));
        }
        return moveBuilder.build();
    }

}
