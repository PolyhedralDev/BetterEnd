package org.polydev.gaea.math;

/**
 * Utility class for mathematical functions.
 */
public class MathUtil {
    /**
     * Gets the standard deviation of an array of doubles.
     * @param numArray The array of numbers to calculate the standard deviation of.
     * @return double - The standard deviation.
     */
    public static double standardDeviation(double[] numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum / length;

        for(double num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }
}
