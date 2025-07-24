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

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Line)) return false;

        Line line = (Line) o;
        return this.getX() == line.getX()
                && this.getY() == line.getY()
                && this.getDirection() == line.getDirection();
    }
    @Override
    public int hashCode(){
        return java.util.Objects.hash(getX(), getY(), getDirection());
    }


}
