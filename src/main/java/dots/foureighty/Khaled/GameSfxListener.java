package dots.foureighty.Khaled;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.listeners.GameUpdateListener;
import dots.foureighty.listeners.GameUpdateType;

import java.util.Objects;

public class GameSfxListener implements GameUpdateListener {
    private final SfxPlayer sfx;
    private final boolean isP1Human;
    private final boolean isP2Human;

    private boolean hasBaseline = false;
    private int lastP1Boxes = 0;
    private int lastP2Boxes = 0;
    private boolean lastPlayer1Turn = true; // who's turn is it?

    public GameSfxListener(SfxPlayer sfx, boolean isP1Human, boolean isP2Human) {
        this.sfx = Objects.requireNonNull(sfx);
        this.isP1Human = isP1Human;
        this.isP2Human = isP2Human;
    }

    @Override public boolean unregister() { return false; }

    @Override
    public synchronized void handleUpdate(GameSnapshot now, GameUpdateType type) {
        if (now == null) return;

        int p1Now = (now.getPlayer1Boxes() == null) ? 0 : now.getPlayer1Boxes().length;
        int p2Now = (now.getPlayer2Boxes() == null) ? 0 : now.getPlayer2Boxes().length;
        boolean p1TurnNow = now.isPlayer1Turn();

        if (!hasBaseline) {
            lastP1Boxes = p1Now;
            lastP2Boxes = p2Now;
            lastPlayer1Turn = p1TurnNow;
            hasBaseline = true;
            return;
        }

        // the sound of sweet victory
        if (type == GameUpdateType.GAME_END) {
            boolean p1Wins = p1Now > p2Now;
            boolean p2Wins = p2Now > p1Now;
            if ((p1Wins && isP1Human) || (p2Wins && isP2Human)) {
                sfx.play(Sfx.DUBS);
            }
            lastP1Boxes = p1Now;
            lastP2Boxes = p2Now;
            lastPlayer1Turn = p1TurnNow;
            return;
        }

        //start of game
        if (type != GameUpdateType.MOVE_PLAYED) {
            lastP1Boxes = p1Now;
            lastP2Boxes = p2Now;
            lastPlayer1Turn = p1TurnNow;
            return;
        }

        // move handling
        int p1Delta = p1Now - lastP1Boxes;
        int p2Delta = p2Now - lastP2Boxes;

        boolean moverWasP1 = lastPlayer1Turn;
        int moverDelta = moverWasP1 ? p1Delta : p2Delta;
        boolean moverIsHuman = moverWasP1 ? isP1Human : isP2Human;

        // Opponent scored
        if (!moverIsHuman && moverDelta >= 1) {
            sfx.play(Sfx.PLAYED_YOURSELF);
        }

        // You scored multiple boxes
        if (moverIsHuman && moverDelta >= 2) {
            sfx.play(Sfx.YOU_SMART);
        }

        // you scored a box
        if (moverIsHuman && moverDelta >= 1) {
            sfx.play(Sfx.ANOTHER_ONE);
        }

        lastP1Boxes = p1Now;
        lastP2Boxes = p2Now;
        lastPlayer1Turn = p1TurnNow;
    }
}
