package me.curtisbradley;

import me.curtisbradley.moves.Line;

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

            game.getBoard().getLines().forEach(line -> {
                g.setColor(line.getPlayer() == Player.PLAYER1 ? Color.RED : Color.BLUE);
                g.fillRoundRect(getXPadding() + line.getX() * getCellDim(), getYPadding() + line.getY() * getCellDim(),
                        line.getDirection() == Line.Direction.DOWN ? 10 : getCellDim() + 10,
                        line.getDirection() == Line.Direction.DOWN ? getCellDim() + 10: 10,10,10);
                    }
            );
            g.setColor(Color.BLACK);
            for (int i = 0; i < game.getXSize(); i++) {
                for (int j = 0; j < game.getYSize(); j++) {
                    g.fillOval(getXPadding() + i * getCellDim(), getYPadding() + j * getCellDim(), 10, 10);
                }
            }


        }

        private ArrayList<AbstractLine> getWalls() {
            ArrayList<AbstractLine> walls = new ArrayList<>();

            for (int i = 0; i < game.getXSize() - 1; i++) {
                for (int j = 0; j < game.getYSize(); j++) {
                    walls.add( new AbstractLine(i,j, Line.Direction.RIGHT, new Point((int) (getXPadding() + i * getCellDim() + (getCellDim() * 0.5)) + 5,
                            getYPadding() + j * getCellDim() + 5)));
                }
            }
            for (int i = 0; i < game.getXSize(); i++) {
                for (int j = 0; j < game.getYSize() - 1; j++) {
                    walls.add(new AbstractLine(i,j, Line.Direction.DOWN, new Point(getXPadding() + i * getCellDim() + 5,
                            (int) (getYPadding() + j * getCellDim() + getCellDim() * 0.5) + 5)));
                }
            }
            return walls;
        }

        private AbstractLine findWall(Point clickedPoint, ArrayList<AbstractLine> validWalls) {
            int tolerance = (int) (getCellDim() * getCellDim() * 0.4);

            return validWalls.stream()
                    .map(abstractLine -> new AbstractMap.SimpleEntry<>(abstractLine, clickedPoint.distanceSq(abstractLine.getGUILocation())))
                    .min(Map.Entry.comparingByValue()).map(pointPair -> pointPair.getValue() > tolerance ? null : pointPair.getKey())
                    .orElse(null);
        }

        private void placeWall(Point clickPoint) {
           AbstractLine nearest = findWall(clickPoint, getWalls());
           if(nearest == null) {
               return;
           }
           game.getBoard().getLines().add(new Line(nearest.getX(),nearest.getY(),nearest.getDirection(),game.getPlayerPlacing()));
           paintComponent(getGraphics());

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

        private static class AbstractLine {
            private final int x;
            private final int y;
            private final Line.Direction direction;
            private final Point GUILocation;

            public AbstractLine(int x, int y, Line.Direction direction, Point GUILocation) {
                this.x = x;
                this.y = y;
                this.direction = direction;
                this.GUILocation = GUILocation;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public Line.Direction getDirection() {
                return direction;
            }

            public Point getGUILocation() {
                return GUILocation;
            }
        }
    }



}
