/**
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestAreaSeq extends LargestArea{

    public static void main(String[] args) {
        LargestAreaSeq largestAreaSeq = new LargestAreaSeq();
        new ArgumentParser(largestAreaSeq).parseArguments(args);
        Point[] points = largestAreaSeq.getPoints();

        System.out.println("*** STARTING SERIAL EXECUTION ***");
        largestAreaSeq.runProgram(points);
    }

    /**
     * Fetch the largest area for these {@code points}. This method will get the largest
     * area sequentially.
     *
     * @param points the points for which the area is to be found.
     */
    @Override
    public AreaResultHolder getLargestArea(Point[] points) {
        return getLargestAreaSequentially(points, 0, points.length - 2);
    }
}
