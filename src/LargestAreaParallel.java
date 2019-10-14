/**
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestAreaParallel extends LargestArea {

    public static void main(String[] args) {
        LargestAreaParallel largestAreaParallel = new LargestAreaParallel();
        new ArgumentParser(largestAreaParallel).parseArguments(args);
        Point[] points = largestAreaParallel.getPoints();

        System.out.println("*** STARTING PARALLEL EXECUTION ***");
        largestAreaParallel.runProgram(points);
    }

    /**
     * Fetch the largest area for these {@code points}. The implementation can be either
     * sequential or parallel.
     *
     * @param points the points for which the area is to be found.
     */
    @Override
    public void getLargestArea(Point[] points) {

    }
}