package dots.foureighty.panels;

import dots.foureighty.gamebuilder.Game;
import dots.foureighty.players.Move;

/***
 * Wrapper that contains a Move.
 * Used for synchronizing receiving a move from a local player.
 *
 * @see dots.foureighty.players.LocalHumanPlayer#getMove(Game)
 */
public class MovePackage {
    private Move move;
    public synchronized void setMove(Move move){
        this.move = move;
    }
    public synchronized Move getMove(){
        return move;
    }
}
