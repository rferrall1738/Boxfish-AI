package me.curtisbradley;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameInterface extends JFrame {

    public GameInterface() {
        super("Game Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250,250);
        setVisible(true);
        setMinimumSize(new Dimension(250,250));
        add(new GridPanel(9,6));
    }


}
class GridPanel extends JPanel {
    private final int xdots;
    private final int ydots;

    public GridPanel(int x, int y) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                checkGridLineClick(clickPoint);
            }
        });
        xdots = x;
        ydots = y;
    }
    private int getYPadding() {
        return (getHeight() - (ydots  - 1) * getCellDim()) / 2;
    }
    private int getXPadding() {
        return (getWidth() - (xdots - 1) * getCellDim()) / 2;
    }

    private int getSpaceSize() {
        return (int) (Math.min(getWidth(), getHeight()) * 0.75);
    }

    private int getCellDim() {
        return getSpaceSize() / (Math.max(xdots,ydots) - 1);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        for (int i = 0; i < xdots; i++) {
            for (int j = 0; j < ydots; j++) {
                g.drawOval( getXPadding() + i * getCellDim(), getYPadding() + j * getCellDim(), 10, 10);
            }

}    }

    private void checkGridLineClick(Point clickPoint) {
        int width = getWidth();
        int height = getHeight();
        int cellWidth = width / xdots;
        int cellHeight = height / ydots;

        int tolerance = 5; // Click tolerance in pixels

        // Check horizontal lines
        for (int i = 0; i <= xdots; i++) {
            int y = i * cellHeight;
            if (Math.abs(clickPoint.y - y) <= tolerance) {
                System.out.println("Clicked on horizontal line " + i);
                return;
            }
        }

        // Check vertical lines
        for (int i = 0; i <= ydots; i++) {
            int x = i * cellWidth;
            if (Math.abs(clickPoint.x - x) <= tolerance) {
                System.out.println("Clicked on vertical line " + i);
                return;
            }
        }

        System.out.println("Clicked outside line segments");
    }
}
