package dots.foureighty.tools;

import java.util.LinkedList;
import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

public class BotBenchmark {

    private final LinkedList<Long> times;

    public BotBenchmark(LinkedList<Long> runTimes) {
        times = (LinkedList<Long>) runTimes.clone();
        times.sort(Long::compareTo);
    }

    private LongStream getStream() {
        return times.stream().mapToLong(item -> item);
    }

    public LongSummaryStatistics getStatistics() {
        return getStream().summaryStatistics();
    }

    public double getStdDev() {
        double mean = getStatistics().getAverage();
        double variance = getStream().mapToDouble((time) -> time - mean)
                .map((timeDifference) -> timeDifference * timeDifference).average().orElse(0);
        return Math.sqrt(variance);
    }

    @Override
    public String toString() {
        return "BotBenchmark [statistics=" + getStatistics() + ", stdDev=" + getStdDev() + "]";
    }
}
