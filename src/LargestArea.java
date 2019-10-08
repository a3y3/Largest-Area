/**
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestArea {
    int numPoints;
    int side;
    int seed;
    boolean verboseOutputs;

    public static void main(String[] args) {
        LargestArea largestArea = new LargestArea();
        new ArgumentParser(largestArea).parseArguments(args);
        System.out.println("Initialized with n = " + largestArea.numPoints + " side" + largestArea.side + " seed" + largestArea.seed);
        RandomPoints r = new RandomPoints(largestArea.numPoints, largestArea.side,
                largestArea.seed);
        Point[] points = largestArea.getPoints(r);
        largestArea.getLargestArea(points);
    }

    private Point[] getPoints(RandomPoints r) {
        Point[] points = new Point[numPoints];
        int i = 0;
        while (r.hasNext()) {
            points[i++] = r.next();
        }
        return points;
    }

    private void getLargestArea(Point[] points) {
        double maxArea = Integer.MIN_VALUE;
        int[] maxIndices = new int[3];
        Point[] maxPoints = new Point[3];

        for (int i = 0; i < points.length - 2; i++) {
            Point p1 = points[i];
            for (int j = i + 1; j < points.length - 1; j++) {
                Point p2 = points[j];
                for (int k = j + 1; k < points.length; k++) {
                    Point p3 = points[k];

                    double a = getLength(p1, p2);
                    double b = getLength(p2, p3);
                    double c = getLength(p3, p1);
                    double S = getS(a, b, c);
                    double area = getArea(S, a, b, c);
                    if (area > maxArea) {
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

    private double getLength(Point p1, Point p2) {
        double y2 = p2.getY();
        double x2 = p2.getX();

        double y1 = p1.getY();
        double x1 = p1.getX();

        double distance = Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2);
        distance = Math.sqrt(distance);
        return distance;
    }

    private double getS(double a, double b, double c) {
        return (a + b + c) / 2.0;
    }

    private double getArea(double S, double a, double b, double c) {
        double area = S * (S - a) * (S - b) * (S - c);
        return Math.sqrt(area);
    }

    private void printPoints(Point... points) {
        for (Point p : points) {
            System.out.print("Point: " + p.getX() + ", " + p.getY() + ";\t");
        }
        System.out.println();
    }
}
