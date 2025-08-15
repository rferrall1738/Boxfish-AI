package dots.foureighty.players.robots.searchbots;

import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveBuilder;

public class DABState {
    private final Board board;
    private final int selfScore;
    private final int opponentScore;
    private final boolean maximizing;

    public DABState(Board board) {
        this.board = board;
        maximizing = true;
        selfScore = 0;
        opponentScore = 0;
    }

    private DABState(Board board, int selfScore, int opponentScore, boolean maximizing) {
        this.board = board;
        this.selfScore = selfScore;
        this.opponentScore = opponentScore;
        this.maximizing = maximizing;
    }

    public int getSelfScore() {
        return selfScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public Board getBoard() {
        return board;
    }

    public DABState withMove(Move move) {
        //TODO Optimize this.
        MoveBuilder builder = new MoveBuilder(board);
        builder.addAll(move.getLines());
        int newBoxes = builder.getNewBoxes().length;

        return new DABState(builder.getCurrentBoard(),
                maximizing ? selfScore + newBoxes : selfScore,
                maximizing ? opponentScore : opponentScore + newBoxes, !maximizing);
    }

    public boolean isMaximizing() {
        return maximizing;
    }
}
