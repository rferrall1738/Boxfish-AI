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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


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
         * Evaluator for a position
         * @param input Final state to evaluate.
         * @return an estimate of this player's score minus the opponent's score.
         */
        @Override
        public float evaluate(MinimaxState input) {
            float score = input.getSelfScore() - input.getOpponentScore();
            for (Heuristic<MinimaxState> heuristic : heuristics) {
                score += 50 * heuristic.evaluate(input);
            }
            return score;
        }
    };


    private final NeighborGenerator neighborGenerator = new NeighborGenerator() {
        @Override
        protected Iterator<Pair<MinimaxState, Move>> getNeighbors(MinimaxState input) {

            final MoveIterator moveIterator = new MoveIterator(input.getBoard());
            List<Pair<MinimaxState, Move>> neighbors = new ArrayList<>();

            while (moveIterator.hasNext()) {
                Move nextMove = moveIterator.next();
                MinimaxState nextState = input.withMove(nextMove);
                neighbors.add(new Pair<>(nextState,nextMove));
            }

            neighbors.sort((a , b) ->{
                float evalA = stateEvaluator.evaluate(a.getKey());
                float evalB = stateEvaluator.evaluate(b.getKey());
                return input.maximizing ? Float.compare(evalB, evalA) : Float.compare(evalA, evalB);
            });

            return neighbors.iterator();
        }
    };

    @Override
    public Move getMove(GameSnapshot gameState) {
        MinimaxState initialState = new MinimaxState(gameState.getBoard()); //TODO

        nodesVisited = 0;
        nodesPruned = 0;
        Pair<LinkedList<Move>, Float> result = search(initialState, neighborGenerator, stateEvaluator, depth, true, -Float.MAX_VALUE, Float.MAX_VALUE);

        System.out.println("Nodes visited: " + getNodesVisited());
        System.out.println("Nodes pruned: " + getNodesPruned());

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

        public boolean isMaximizing() {return maximizing;}

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
