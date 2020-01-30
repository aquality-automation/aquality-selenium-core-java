package utils;

public class TimeUtil {

    public static double getCurrentTimeInSeconds() {
        return System.nanoTime() / Math.pow(10, 9);
    }
}
