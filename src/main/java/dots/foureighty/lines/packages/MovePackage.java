package dots.foureighty.lines.packages;

import dots.foureighty.lines.Move;

/***
 * Wrapper that contains a Move.
 * Used for synchronizing receiving a move from a local player.
 *
 * @see dots.foureighty.players.LocalHumanPlayer#getMove(dots.foureighty.game.GameSnapshot)
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
