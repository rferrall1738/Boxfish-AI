package dots.foureighty.players.robots;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Line;
import dots.foureighty.lines.Move;
import dots.foureighty.players.Player;
import dots.foureighty.util.Pair;

import java.awt.*;


public class MinimaxBot extends Player {
    public MinimaxBot(int depth) {
        super("MinimaxBot Depth " + depth, Color.PINK);

    }

    @Override
    public Move getMove(GameSnapshot gameState) {
        return searchForMove(gameState.getBoard(), true, 3).getKey();
    }

    GreedyBot greedyBot = new GreedyBot();

    private Pair<Move, Integer> searchForMove(Board gameBoard, boolean isMaxing, int depth) {
        if (depth == 0) {
            Move m = greedyBot.getMaximizingMove(gameBoard);
            return new Pair<>(m, evaluateMove(m, gameBoard));
        }
        return null;
    }

    private int evaluateMove(Move move, Board board) {
        int score = 0;
        Board currentBoard = board;
        for (Line l : move.getLines()) {
            score += board.getCompletedBoxes(l).size();
            currentBoard = currentBoard.append(l);
        }
        return score;
    }
}
