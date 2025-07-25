package dots.foureighty.panels;

import dots.foureighty.gamebuilder.NewGame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InteractablePanel extends GamePanel {

    public InteractablePanel(NewGame game, MoveListener listener) {
        super(game);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                //  placeWall(clickPoint);
            }
        });
    }
    /* TODO
    private ArrayList<GUILine> getWalls() {
        ArrayList<GUILine> walls = new ArrayList<>();

        game.getGameBoard().getValidLinePlacements().forEach(line -> {
            Point point = line.getDirection() == Line.Direction.RIGHT
                    ? new Point((int) (getXPadding() + line.getX() * getCellDim() + (getCellDim() * 0.5)) + 5,
                    getYPadding() + line.getY() * getCellDim() + 5)
                    : new Point(getXPadding() + line.getX() * getCellDim() + 5,
                    (int) (getYPadding() + line.getY() * getCellDim() + (getCellDim() * 0.5)) + 5);
            walls.add(new GUILine(line.getX(), line.getY(), line.getDirection(), point));
        });
        return walls;
    }

    private GUILine findWall(Point clickedPoint, ArrayList<GUILine> validWalls) {
        int tolerance = (int) (getCellDim() * 0.9); // easier click
        return validWalls.stream()
                .map(w -> new AbstractMap.SimpleEntry<>(w, clickedPoint.distance(w.getGUILocation())))
                .min(Map.Entry.comparingByValue())
                .map(e -> e.getValue() > tolerance ? null : e.getKey())
                .orElse(null);
    }

    private void placeWall(Point clickPoint) {
        GUILine nearest = findWall(clickPoint, getWalls());
        if (nearest == null) {
            return;
        }

        PlayedLine lineToPlay = new PlayedLine(nearest.getX(), nearest.getY(), nearest.getDirection(), game.getPlayerPlacing());

        if (game.getBoard().getPlayedLines().contains(lineToPlay)) {
            return;
        }

        game.getBoard().addLine(lineToPlay);
        game.setPlayerPlacing(game.getPlayerPlacing() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1);
        repaint();
    }

     */
}
