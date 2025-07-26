package dots.foureighty.panels;

import dots.foureighty.gamebuilder.Board;
import dots.foureighty.gamebuilder.Game;
import dots.foureighty.lines.BoxSide;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;

/***
 * JPanel component that receives input from the player and passes the move to the Game instance
 * @see GamePanel Displays the board without interaction
 */
class InteractableGamePanel extends GamePanel {
    private boolean moveComplete = false;
    private final ArrayList<Line> linesToPlace = new ArrayList<>();
    private final ArrayList<Point> newBoxes = new ArrayList<>();
    private final MoveStatusListener moveStatusListener;
    /***
     * Interface to allow the parent to be notified when a move is ready to be sent.
     */
    public interface MoveStatusListener {
        void handleStatusUpdate();
    }

    /***
     *Creates an interactable panel to send a move to the game instance.
     * @param game Dots and boxes game instance
     * @param moveStatusListener method that is called when the gameboard has a valid move queued
     */
    public InteractableGamePanel(Game game, MoveStatusListener moveStatusListener) {
        super(game);
        this.moveStatusListener = moveStatusListener;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEnabled() && !isMoveComplete()){
                    Point clickPoint = e.getPoint();
                    placeLine(clickPoint);
                }
            }
        });
    }

    public void setIsMoveComplete(boolean moveComplete) {
        this.moveComplete = moveComplete;
        moveStatusListener.handleStatusUpdate();
    }
    public boolean isMoveComplete(){
        return moveComplete;
    }

    /***
     * Gets the locations on a canvas where a wall could be placed.
     * @return A list of GUILines
     * @see GUILine
     */
    private ArrayList<GUILine> getValidLines() {
        ArrayList<GUILine> walls = new ArrayList<>();
        game.getGameBoard().getValidLinePlacements().forEach(line -> {
            Point point = line.getDirection() == LineDirection.RIGHT
                    ? new Point((int) (getXPadding() + line.getX() * getCellDim() + (getCellDim() * 0.5)) + 5,
                    getYPadding() + line.getY() * getCellDim() + 5)
                    : new Point(getXPadding() + line.getX() * getCellDim() + 5,
                    (int) (getYPadding() + line.getY() * getCellDim() + (getCellDim() * 0.5)) + 5);
            walls.add(new GUILine(line.getX(), line.getY(), line.getDirection(), point));
        });
        return walls;
    }



    /***
     * Finds the wall closest to the point clicked
     * @param clickedPoint The point on the canvas clicked
     * @param validWalls The possible lines that could be played
     * @return The closest wall to the point
     */
    private GUILine findLineNearPoint(Point clickedPoint, ArrayList<GUILine> validWalls) {
        int tolerance = (int) (getCellDim() * 0.9); // easier click
        return validWalls.stream()
                .map(w -> new AbstractMap.SimpleEntry<>(w, clickedPoint.distance(w.getGUILocation())))
                .min(Map.Entry.comparingByValue())
                .map(e -> e.getValue() > tolerance ? null : e.getKey())
                .orElse(null);
    }
    private Color getPlayersColor(){
        return (game.isPlayer1Turn() ? game.getPlayer1() : game.getPlayer2()).getColor();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color color = getPlayersColor();
        Color transparentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);
        g.setColor(transparentColor);
        linesToPlace.forEach((line) -> this.drawNewLines(g,line));
    }

    private void drawNewLines(Graphics g, Line line) {
        g.fillRoundRect(getXPadding() + line.getX() * getCellDim(), getYPadding() + line.getY() * getCellDim(),
                line.getDirection() == LineDirection.DOWN ? 10 : getCellDim() + 10,
                line.getDirection() == LineDirection.DOWN ? getCellDim() + 10 : 10, 20, 20);
    }
    private void drawNewBoxes(Graphics g, Point cornerOfBox) {
        //TODO: Implement
    }

    private void placeLine(Point clickPoint) {
        GUILine nearest = findLineNearPoint(clickPoint, getValidLines());
        if (nearest == null) {
            return;
        }
        if (linesToPlace.contains(nearest) || game.getGameBoard().containsLine(nearest)) {
            return;
        }
        Board futureBoard = game.getGameBoard();

        for (Line l : linesToPlace) {
            futureBoard = futureBoard.addLine(l);
        }

        linesToPlace.add(nearest);
        if (linesToPlace.size() == 1) {
            moveStatusListener.handleStatusUpdate();
        }

        EnumSet<BoxSide> completedBoxes = futureBoard.getCompletedBoxes(nearest);

        if (completedBoxes.isEmpty()) {
            setIsMoveComplete(true);
        } else {
            if (completedBoxes.contains(BoxSide.LEFT)) {
                newBoxes.add(new Point(nearest.getX() - 1, nearest.getY()));
            }
            if (completedBoxes.contains(BoxSide.RIGHT) || completedBoxes.contains(BoxSide.BELOW)) {
                newBoxes.add(new Point(nearest.getX(), nearest.getY()));
            }
            if (completedBoxes.contains(BoxSide.ABOVE)) {
                newBoxes.add(new Point(nearest.getX(), nearest.getY() + 1));
            }
        }
        if (linesToPlace.size() == game.getGameBoard().getLineBitSet().cardinality()) {
            setIsMoveComplete(true);
        }

        repaint();
    }

    public ArrayList<Line> getNewLines() {
        return linesToPlace;
    }

    public void clearQueuedMove() {
        linesToPlace.clear();
        newBoxes.clear();
        setIsMoveComplete(false);
        repaint();
    }



    /***
     * Utility class for mapping the location of a line to the game canvas.
     */
    class GUILine extends Line {
        private final Point GUILocation;

        public GUILine(int x, int y, LineDirection direction, Point GUILocation) {
            super(x, y, direction);
            this.GUILocation = GUILocation;
        }

        public Point getGUILocation() {
            return GUILocation;
        }
    }


}
