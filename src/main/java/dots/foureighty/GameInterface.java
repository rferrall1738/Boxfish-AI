// File: GameInterface.java
package dots.foureighty;

import javax.swing.*;

public class GameInterface extends JFrame {
        /*
    public GameInterface(Game g) {
        super("DOTS AND BOXES");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridPanel panel = new GridPanel(g);
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setBackground(Color.WHITE);
        add(panel);

        pack();
        panel.repaint();
        setVisible(true);
    }

    static class GridPanel extends JPanel {
        private final Game game;

        public GridPanel(Game game) {
            this.game = game;
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint = e.getPoint();
                    System.out.println("Clicked: " + clickPoint);
                    placeWall(clickPoint);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw played lines
            game.getBoard().getPlayedLines().forEach(playedLine -> {
                g.setColor(playedLine.getPlayer() == Player.PLAYER1 ? Color.RED : Color.BLUE);
                g.fillRoundRect(getXPadding() + playedLine.getX() * getCellDim(), getYPadding() + playedLine.getY() * getCellDim(),
                        playedLine.getDirection() == Line.Direction.DOWN ? 10 : getCellDim() + 10,
                        playedLine.getDirection() == Line.Direction.DOWN ? getCellDim() + 10 : 10, 20, 20);
            });

            // Draw dots
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

     */
}
