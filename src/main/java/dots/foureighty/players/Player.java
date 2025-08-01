package dots.foureighty.players;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.lines.Move;

import java.awt.*;

public interface Player {


    Color getColor();

    void setColor(Color color);

    String getName();

    Move getMove(GameSnapshot gameState);


}
