package dots.foureighty.listeners;

import dots.foureighty.game.GameSnapshot;

public interface GameUpdateListener {
    void handleUpdate(GameSnapshot gameSnapshot, GameUpdateType gameUpdateType);

    default boolean unregister() {
        return false;
    }

}
