package dots.foureighty.gui.ui;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.gui.GamePanel;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.LineDirection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

/***
 * JPanel component that receives input from the player and passes the move to the Game instance
 * @see GamePanel Displays the board without interaction
 */
class InteractableGamePanel extends GamePanel {
    private boolean moveComplete = false;
    private final ArrayList<Line> queuedLines = new ArrayList<>();
    private final ArrayList<Point> queuedBoxes = new ArrayList<>();
    private final MoveStatusListener moveStatusListener;

    private static final float TEMP_LINE_ALPHA = 0.7f;
    private static final float TEMP_BOX_ALPHA = 0.3f;

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
    public InteractableGamePanel(GameSnapshot game, MoveStatusListener moveStatusListener) {
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
        game.getBoard().getUnplayedPositions().forEach(line -> {
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
        return game.isPlayer1Turn() ? game.getPlayer1Color() : game.getPlayer2Color();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(setAlpha(getPlayersColor(), TEMP_BOX_ALPHA).darker());
        queuedBoxes.forEach((newBox) -> drawBox(g, newBox));

        g.setColor(setAlpha(getPlayersColor(), TEMP_LINE_ALPHA));
        queuedLines.forEach((line) -> drawLine(g, line));

    }


    private void placeLine(Point clickPoint) {
        GUILine nearest = findLineNearPoint(clickPoint, getValidLines());
        if (nearest == null) {
            return;
        }
        if (queuedLines.contains(nearest) || game.getBoard().containsLine(nearest)) {
            return;
        }
        Board futureBoard = game.getBoard();

        for (Line l : queuedLines) {
            futureBoard = futureBoard.append(l);
        }

        queuedLines.add(nearest);
        if (queuedLines.size() == 1) {
            moveStatusListener.handleStatusUpdate();
        }

        ArrayList<Point> newBoxes = futureBoard.getCompletedBoxLocations(nearest);
        queuedBoxes.addAll(newBoxes);
        if (newBoxes.isEmpty() || futureBoard.getUnplayedPositions().size() == 1) {
            setIsMoveComplete(true);
        }


        repaint();
    }

    public ArrayList<Line> getNewLines() {
        return queuedLines;
    }

    public void clearQueuedMove() {
        queuedLines.clear();
        queuedBoxes.clear();
        setIsMoveComplete(false);
        repaint();
    }



    /***
     * Utility class for mapping the location of a line to the game canvas.
     */
    static class GUILine extends Line {
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
