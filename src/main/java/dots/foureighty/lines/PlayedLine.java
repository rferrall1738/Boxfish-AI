package dots.foureighty.lines;

import dots.foureighty.Player;

public class PlayedLine extends Line {
    private final Player player;

    public PlayedLine(int x, int y, Direction direction, Player player) {
        super(x,y,direction);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }


}
