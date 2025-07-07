package dots.foureighty;

import dots.foureighty.lines.GUILine;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.PlayedLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class GameInterface extends JFrame {

    public GameInterface(Game g) {
        super("Game Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 250);
        setVisible(true);
        setMinimumSize(new Dimension(250, 250));
        add(new GridPanel(g));
    }

    static class GridPanel extends JPanel {
        private final Game game;

        public GridPanel(Game game) {
            this.game = game;
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint = e.getPoint();
                    placeWall(clickPoint);
                }
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            game.getBoard().getPlayedLines().forEach(playedLine -> {
                g.setColor(playedLine.getPlayer() == Player.PLAYER1 ? Color.RED : Color.BLUE);
                g.fillRoundRect(getXPadding() + playedLine.getX() * getCellDim(), getYPadding() + playedLine.getY() * getCellDim(),
                        playedLine.getDirection() == Line.Direction.DOWN ? 10 : getCellDim() + 10,
                        playedLine.getDirection() == Line.Direction.DOWN ? getCellDim() + 10: 10,10,10);
                    }
            );
            g.setColor(Color.BLACK);
            for (int i = 0; i < game.getXSize(); i++) {
                for (int j = 0; j < game.getYSize(); j++) {
                    g.fillOval(getXPadding() + i * getCellDim(), getYPadding() + j * getCellDim(), 10, 10);
                }
            }


        }

        private ArrayList<GUILine> getWalls() {
            ArrayList<GUILine> walls = new ArrayList<>();

            game.getBoard().getValidLinePlacements().forEach(line -> {
                if(line.getDirection() == Line.Direction.RIGHT) {
                    walls.add(new GUILine(line.getX(), line.getY(), line.getDirection(),
                            new Point((int) (getXPadding() + line.getX() * getCellDim() + (getCellDim() * 0.5)) + 5,
                                    getYPadding() + line.getY() * getCellDim() + 5)));
                } else if(line.getDirection() == Line.Direction.DOWN) {
                    walls.add(new GUILine(line.getX(), line.getY(), line.getDirection(),
                            new Point(getXPadding() + line.getX() * getCellDim() + 5,
                                    (int) (getYPadding() + line.getY() * getCellDim() + (getCellDim() * 0.5)) + 5)));
                }

            });
            return walls;
        }

        private GUILine findWall(Point clickedPoint, ArrayList<GUILine> validWalls) {
            int tolerance = (int) (getCellDim() * 0.25);

            return validWalls.stream()
                    .map(GUILine -> new AbstractMap.SimpleEntry<>(GUILine, clickedPoint.distance(GUILine.getGUILocation())))
                    .min(Map.Entry.comparingByValue()).map(pointPair -> pointPair.getValue() > tolerance ? null : pointPair.getKey())
                    .orElse(null);
        }

        private void placeWall(Point clickPoint) {
           GUILine nearest = findWall(clickPoint, getWalls());
           if(nearest == null) {
               return;
           }
           game.getBoard().playLine((new PlayedLine(nearest.getX(),nearest.getY(),nearest.getDirection(),game.getPlayerPlacing())));
           paintComponent(getGraphics());
           game.setPlayerPlacing(game.getPlayerPlacing() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1);

        }

        private int getYPadding() {
            return (getHeight() - (game.getYSize() - 1) * getCellDim()) / 2;
        }

        private int getXPadding() {
            return (getWidth() - (game.getYSize() - 1) * getCellDim()) / 2;
        }

        private int getSpaceSize() {
            return (int) (Math.min(getWidth(), getHeight()) * 0.75);
        }

        private int getCellDim() {
            return getSpaceSize() / (Math.max(game.getXSize(), game.getYSize()) - 1);
        }

    }

}
