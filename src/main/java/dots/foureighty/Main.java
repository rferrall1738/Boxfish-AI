package dots.foureighty;


import dots.foureighty.gamebuilder.GameFactory;
import dots.foureighty.players.RandomBot;

import javax.swing.*;

public class Main {
    public static final JFrame MAIN_FRAME = new JFrame("BoxFish");
    public static void main(String[] args) {
        MAIN_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_FRAME.setResizable(false);
        new GameFactory().setXSize(5).setYSize(5).setPlayer2(new RandomBot()).build().play();
    }
}