package dots.foureighty.players.robots.searchbots.minimax;

import dots.foureighty.game.GameSnapshot;
import dots.foureighty.game.boards.Board;
import dots.foureighty.lines.Move;
import dots.foureighty.lines.MoveBuilder;
import dots.foureighty.lines.MoveIterator;
import dots.foureighty.players.Player;
import dots.foureighty.players.robots.Heuristic;
import dots.foureighty.players.robots.algorithms.MinimaxSearchAlgorithm;
import dots.foureighty.util.ColorUtils;
import dots.foureighty.util.Pair;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;


public class MinimaxBot extends MinimaxSearchAlgorithm<MinimaxBot.MinimaxState, Move> implements Player {
    private final int depth;
    private Color color = Color.ORANGE;
    private final Heuristic<MinimaxState>[] heuristics;

    public MinimaxBot(int depth, Heuristic<MinimaxState>... heuristics) {
        this.depth = depth;
        this.heuristics = heuristics;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = ColorUtils.withFullAlpha(color);
    }

    @Override
    public String getName() {
        return "MinimaxBot (" + depth + ")";
    }

    private final Evaluator stateEvaluator = new Evaluator() {

        /***
         * Evaluator for a possition
         * @param input Final state to evaluate.
         * @return an estimate of this player's score minus the opponent's score.
         */
        @Override
        protected float evaluate(MinimaxState input) {
            float score = input.getSelfScore() - input.getOpponentScore();
            for (Heuristic<MinimaxState> heuristic : heuristics) {
                score += heuristic.evaluate(input);
            }
            return score;
        }
    };


    private final NeighborGenerator neighborGenerator = new NeighborGenerator() {
        @Override
        protected Iterator<Pair<MinimaxState, Move>> getNeighbors(MinimaxState input) {
            final MoveIterator moveIterator = new MoveIterator(input.getBoard());
            return new Iterator<Pair<MinimaxState, Move>>() {

                @Override
                public boolean hasNext() {
                    return moveIterator.hasNext();
                }

                @Override
                public Pair<MinimaxState, Move> next() {
                    Move nextMove = moveIterator.next();
                    return new Pair<>(input.withMove(nextMove), nextMove);
                }
            };
        }
    };

    @Override
    public Move getMove(GameSnapshot gameState) {
        MinimaxState initialState = new MinimaxState(gameState.getBoard()); //TODO
        Pair<LinkedList<Move>, Float> result = search(initialState, neighborGenerator, stateEvaluator, depth, true);

        return result.getKey().getFirst();
    }


    public static class MinimaxState {
        private final Board board;
        private final int selfScore;
        private final int opponentScore;
        private final boolean maximizing;

        public MinimaxState(Board board) {
            this.board = board;
            maximizing = true;
            selfScore = 0;
            opponentScore = 0;
        }

        private MinimaxState(Board board, int selfScore, int opponentScore, boolean maximizing) {
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

        public MinimaxState withMove(Move move) {
            //TODO Optimize this.
            MoveBuilder builder = new MoveBuilder(board);
            builder.addAll(move.getLines());
            int newBoxes = builder.getNewBoxes().length;

            return new MinimaxState(builder.getCurrentBoard(),
                    maximizing ? selfScore + newBoxes : selfScore,
                    maximizing ? opponentScore : opponentScore + newBoxes, !maximizing);
        }
    }
}
