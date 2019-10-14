import java.util.Arrays;

/**
 * @author Soham Dongargaonkar [sd4324] on 12/10/19
 */
abstract class LargestArea implements LargestAreaInterface {
    int numPoints;
    int side;
    int seed;
    boolean verboseOutputs;

    /**
     * Runs the program and calculates the time required to execute it.
     *
     * @param points The array of points for which the largest area is to be found.
     */
    public void runProgram(Point[] points) {
        long startTime = System.nanoTime();
        AreaResultHolder resultHolder = getLargestArea(points);
        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;
        resultHolder.printMaxArea();
        System.out.println("Time taken for execution: " + elapsedTime / 1000000 + " ms");
    }

    /**
     * Fetch the largest area for these {@code points}. This class will get the largest
     * area sequentially.
     *
     * @param points the points for which the area is to be found.
     */
    AreaResultHolder getLargestAreaSequentially(Point[] points, int startingIndex,
                                                int endIndex) {
        double maxArea = Integer.MIN_VALUE;
        int[] maxIndices = new int[3];
        Point[] maxPoints = new Point[3];

        for (int i = startingIndex; i < endIndex; i++) {
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
        return new AreaResultHolder(maxPoints, maxIndices, maxArea);
    }

    /**
     * Fetches a number of random points.
     *
     * @return an array of random points.
     */
    @Override
    public Point[] getPoints() {
        RandomPoints r = new RandomPoints(numPoints, side, seed);
        Point[] points = new Point[numPoints];
        int i = 0;
        while (r.hasNext()) {
            points[i++] = r.next();
        }
        return points;
    }

    /**
     * Get the Euclidean length between points p1 and p2.
     *
     * @param p1 first point
     * @param p2 second point
     * @return the distance between them.
     */
    @Override
    public double getLength(Point p1, Point p2) {
        double y2 = p2.getY();
        double x2 = p2.getX();

        double y1 = p1.getY();
        double x1 = p1.getX();

        double distance = Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2);
        distance = Math.sqrt(distance);
        return distance;
    }

    /**
     * Calculates S using the formula S = (a + b + c) / 2.
     *
     * @param a length 1 of the triangle
     * @param b length 2 of the triangle
     * @param c length 3 of the triangle
     * @return S for calculating the area.
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
    public void printPoints(Point... points) {
        for (Point p : points) {
            System.out.print("Point: (" + p.getX() + ", " + p.getY() + ");\t");
        }
        System.out.println();
    }

    /**
     * According to the problem definition:
     * If more than one triangle has the same largest area, the program must print the
     * triangle with the smallest first index. If more than one triangle has the same
     * largest area and smallest first index, the program must print the triangle with the
     * smallest second index. If more than one triangle has the same largest area and
     * smallest first index and smallest second index, the program must print the triangle
     * with the smallest third index.
     *
     * @param oldIndices the array of the earlier highest area points.
     * @param newIndices the array of the new points.
     * @return true if the new indices (and the newer area) need to replace the old
     * indices.
     */
    boolean replaceWithNewerPoints(int[] oldIndices, int[] newIndices) {
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
}
