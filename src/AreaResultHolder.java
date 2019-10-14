import java.util.ArrayList;

/**
 * @author Soham Dongargaonkar [sd4324] on 14/10/19
 */
class AreaResultHolder {
    private Point[] maxPoints;
    private int[] maxIndices;
    private double maxArea;

    private final int POINTS_LENGTH = 3;
    private final int INDICES_LENGTH = 3;
    private final int MAX_AREA_LENGTH = 1;
    private final int POINTS_OFFSET = 0;
    private final int INDICES_OFFSET = 6;
    private final int AREA_OFFSET = 9;

    public AreaResultHolder(Point[] maxPoints, int[] maxIndices, double maxArea) {
        this.maxPoints = maxPoints;
        this.maxIndices = maxIndices;
        this.maxArea = maxArea;
    }

    private double[] getArray() {
        int lengthsSum = POINTS_LENGTH * 2 + INDICES_LENGTH + MAX_AREA_LENGTH;
        int counter = 0;
        double[] arr = new double[lengthsSum];

        for (Point maxPoint : maxPoints) {
            arr[counter++] = (maxPoint.getX());
            arr[counter++] = (maxPoint.getY());
        }
        for (int i : maxIndices) {
            arr[counter++] = i;
        }
        arr[counter] = maxArea;
        return arr;
    }

    private AreaResultHolder getHolder(double[] arr) {
        Point[] points = new Point[POINTS_LENGTH];
        int pCounter = 0;
        for (int d = POINTS_OFFSET; d < POINTS_OFFSET + POINTS_LENGTH * 3; d += 2) {
            double x = arr[d];
            double y = arr[d + 1];
            Point p = new Point(x, y);
            points[pCounter++] = p;
        }

        int[] indices = new int[INDICES_LENGTH];
        int iCounter = 0;
        for (int i = INDICES_OFFSET; i < INDICES_OFFSET + INDICES_LENGTH; i++) {
            indices[iCounter++] = (int) arr[i];
        }

        double area = arr[AREA_OFFSET];
        return new AreaResultHolder(points, indices, area);
    }

    /**
     * Prints the max area with a pre-specified format.
     */
    public void printMaxArea() {
        int indexCounter = 0;
        for (Point p : maxPoints) {
            System.out.printf("%d %.5g %.5g%n", maxIndices[indexCounter++], p.getX(),
                    p.getY());
        }
        System.out.printf("%.5g%n", maxArea);
    }
}
