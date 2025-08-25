package botbenchmark;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public    class BotStats {
    private final int games;
    private final int[] p1Boxes;
    private final int[] p2Boxes;
    public BotStats(int games, int[] p1Boxes, int[] p2Boxes) {
        this.games = games;
        this.p1Boxes = p1Boxes;
        this.p2Boxes = p2Boxes;

        if (p1Boxes.length != p2Boxes.length) {
            throw new IllegalArgumentException("Boxes are not of the same length");
        }
    }
    public IntSummaryStatistics getP1BoxesStats() {
        return Arrays.stream(p1Boxes).summaryStatistics();
    }
    public IntSummaryStatistics getP2BoxesStats() {
        return Arrays.stream(p2Boxes).summaryStatistics();
    }
    public int getNumberOfGames() {
        return games;
    }
    public int getNumberOfP1Wins() {
        int wins = 0;
        for (int i = 0; i < p1Boxes.length; i++) {
            if (p1Boxes[i] > p2Boxes[i]) {
                wins++;
            }
        }
        return wins;
    }
}