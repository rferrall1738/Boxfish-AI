package dots.foureighty;


import dots.foureighty.game.GameFactory;
import dots.foureighty.game.boards.StandardBoards;
import dots.foureighty.gui.GameWatcher;
import dots.foureighty.lines.Move;
import dots.foureighty.players.robots.dumb.GreedyBot;
import dots.foureighty.players.robots.searchbots.DABState;
import dots.foureighty.players.robots.searchbots.mcts.MCTSBot;
import dots.foureighty.players.robots.searchbots.minimax.AlphaBetaBot;
import dots.foureighty.gui.StartScreen;

import dots.foureighty.players.robots.algorithms.mcts.MCTSSearchAlgorithm;
import dots.foureighty.players.robots.searchbots.mctsmax.MCTSMaxBot;
import dots.foureighty.players.robots.searchbots.minimax.ParallelMaxBot;



public class Main {
    public static void main(String[] args) {
        // Uncomment for main screen
        //new StartScreen();


        new GameFactory().withXSize(7).withYSize(7)
                    .withUpdateListener(new GameWatcher())
                    .withBoardGenerator(StandardBoards.AMERICAN)
                .withPlayer1(new MCTSBot(1000))
                .withPlayer2(new MCTSMaxBot((new MCTSSearchAlgorithm<DABState, Move>(1000){}),3, 0.5))
                .build().play();
    }

}