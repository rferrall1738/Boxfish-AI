package dots.foureighty;


import dots.foureighty.gamebuilder.GameFactory;

import javax.swing.*;

public class Main {
    public static JFrame MAIN_FRAME = new JFrame("BoxFish");
    public static void main(String[] args) {
        new GameFactory().setXSize(5).setYSize(5).build().play();


    }
}