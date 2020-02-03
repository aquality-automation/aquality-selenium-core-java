package utils;

public class Timer {
    private double startTime;

    public void start() {
        if (startTime == 0) {
            startTime = getCurrentTimeInSeconds();
        }
    }

    public double stop() {
        return getCurrentTimeInSeconds() - startTime;
    }

    private static double getCurrentTimeInSeconds() {
        return System.nanoTime() / Math.pow(10, 9);
    }
}