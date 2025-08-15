package dots.foureighty.players.robots.heuristics;

import dots.foureighty.lines.Line;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.searchbots.DABState;

import java.awt.*;
import java.util.List;


public class ChainHeuristic implements Heuristic<DABState> {

    @Override
    public float evaluate(DABState state) {
        float score = 0f;
        List<Point> boxes = state.getBoard().getBoxes();

        for (Point box : boxes) {
            List<Line> lines = state.getBoard().getBoxesLines(box);
            int filledLines = 0;
            for (Line line : lines) {
                if (state.getBoard().containsLine(line)) {
                    filledLines++;
                }
            }
            if (filledLines == 2 && !state.isMaximizing()) {
                score -= -50f;
            }
            if (filledLines == 3) {
                score += 100f;
            }
        }
        return score;
    }
}