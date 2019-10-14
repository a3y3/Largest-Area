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
     * Fetch the largest area for these {@code points}. This class will get the largest
     * are sequentially.
     *
     * @param points the points for which the area is to be found.
     */
    public void getLargestArea(Point[] points) {
        double maxArea = Integer.MIN_VALUE;
        int[] maxIndices = new int[3];
        Point[] maxPoints = new Point[3];

        for (int i = 0; i < points.length - 2; i++) {
            Point p1 = points[i];
            for (int j = i + 1; j < points.length - 1; j++) {
                Point p2 = points[j];
                for (int k = j + 1; k < points.length; k++) {
                    Point p3 = points[k];

                    double area = getArea(p1, p2, p3);
                    if (area >= maxArea) {
                        if (area == maxArea) {
                            int[] oldIndices = new int[3];
                            System.arraycopy(maxIndices, 0, oldIndices, 0, 3);
                            int[] newIndices = new int[]{i, j, k};
                            if (!replaceWithNewerPoints(oldIndices, newIndices)) {
                                continue;
                            }
                        }
                        maxArea = area;
                        maxPoints[0] = p1;
                        maxIndices[0] = i;

                        maxPoints[1] = p2;
                        maxIndices[1] = j;

                        maxPoints[2] = p3;
                        maxIndices[2] = k;
                    }
                }
            }
        }
        printMaxArea(maxPoints, maxIndices, maxArea);
    }
}
