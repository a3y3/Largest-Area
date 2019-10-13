import java.util.Arrays;

/**
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestAreaParallel extends LargestArea implements LargestAreaInterface{

    public static void main(String[] args) {
        LargestAreaParallel largestAreaParallel = new LargestAreaParallel();
        new ArgumentParser(largestAreaParallel).parseArguments(args);
        Point[] points = largestAreaParallel.getPoints();

        System.out.println("*** STARTING PARALLEL EXECUTION ***");
        largestAreaParallel.runProgram(points);
        System.out.println();
    }

    /**
     * Runs the program and calculates the time required to execute it.
     *
     * @param points The array of points for which the largest area is to be found.
     */
    private void runProgram(Point[] points) {
        long startTime = System.nanoTime();
        getLargestArea(points);
        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;
        System.out.println("Time taken for execution:" + elapsedTime / 1000000 + " ms");
    }

    public Point[] getPoints() {
        RandomPoints r = new RandomPoints(numPoints, side, seed);
        Point[] points = new Point[numPoints];
        int i = 0;
        while (r.hasNext()) {
            points[i++] = r.next();
        }
        return points;
    }

    @Override
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
        int indexCounter = 0;
        for (Point p : maxPoints) {
            System.out.printf("%d %.5g %.5g%n", maxIndices[indexCounter++], p.getX(),
                    p.getY());
        }
        System.out.printf("%.5g%n", maxArea);
    }

    private boolean replaceWithNewerPoints(int[] oldIndices, int[] newIndices) {
        Arrays.sort(oldIndices);
        Arrays.sort(newIndices);

        for (int i = 0; i < newIndices.length; i++) {
            if (newIndices[i] < oldIndices[i]) {
                return true;
            } else if (newIndices[i] > oldIndices[i]) {
                return false;
            }
        }
        return false;
    }

    public double getLength(Point p1, Point p2) {
        double y2 = p2.getY();
        double x2 = p2.getX();

        double y1 = p1.getY();
        double x1 = p1.getX();

        double distance = Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2);
        distance = Math.sqrt(distance);
        return distance;
    }

    public double getS(double a, double b, double c) {
        return (a + b + c) / 2.0;
    }

    /**
     * Calculates area according to the formula area = S(S-a)(S-b)(S-c) ^ 0.5
     *
     * @param S (a+b+c)/2 as calculated by {@code getS()}
     * @param a length of side 1
     * @param b length of side 2
     * @param c length of side 3
     * @return the calculated area
     */
    public double calculateArea(double S, double a, double b, double c) {
        double area = S * (S - a) * (S - b) * (S - c);
        return Math.sqrt(area);
    }

    /**
     * Calls {@code getLength()} and {@code getS()} to find {@code a, b, c, S} and
     * returns the value calculated by {@code calculateArea()}.
     *
     * @param p1 Point p1
     * @param p2 Point p2
     * @param p3 Point p3
     * @return the area of the triangle formed by points {@code p1, p2, p3}.
     */
    public double getArea(Point p1, Point p2, Point p3) {
        double a = getLength(p1, p2);
        double b = getLength(p2, p3);
        double c = getLength(p3, p1);
        double S = getS(a, b, c);

        return calculateArea(S, a, b, c);
    }

    /**
     * Prints the x and y coordinate of a variable number of Points.
     *
     * @param points variable number of points that are to be printed.
     */
    public void printPoints(Point... points) {
        for (Point p : points) {
            System.out.print("Point: " + p.getX() + ", " + p.getY() + ";\t");
        }
        System.out.println();
    }
}
