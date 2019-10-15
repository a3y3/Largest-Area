/**
 * @author Soham Dongargaonkar [sd4324] on 12/10/19
 */
public interface LargestAreaInterface {
    /**
     * Fetches a number of random points.
     *
     * @return an array of random points.
     */
    Point[] getPoints();

    /**
     * Runs the program and calculates the time required to execute it.
     *
     * @param points The array of points for which the largest area is to be found.
     */
    void runProgram(Point[] points);

    /**
     * Fetch the largest area for these {@code points}. The implementation can be either
     * sequential or parallel.
     *
     * @param points the points for which the area is to be found.
     */
    AreaResultHolder getLargestArea(Point[] points);

    /**
     * Get the Euclidean length between points p1 and p2.
     *
     * @param p1 first point
     * @param p2 second point
     * @return the distance between them.
     */
    double getLength(Point p1, Point p2);

    /**
     * Calculates S using the formula S = (a + b + c) / 2.
     *
     * @param a length 1 of the triangle
     * @param b length 2 of the triangle
     * @param c length 3 of the triangle
     * @return S for calculating the area.
     */
    double getS(double a, double b, double c);

    /**
     * Calculates area according to the formula area = S(S-a)(S-b)(S-c) ^ 0.5
     *
     * @param S (a+b+c)/2 as calculated by {@code getS()}
     * @param a length of side 1
     * @param b length of side 2
     * @param c length of side 3
     * @return the calculated area
     */
    double calculateArea(double S, double a, double b, double c);

    /**
     * Calls {@code getLength()} and {@code getS()} to find {@code a, b, c, S} and
     * returns the value calculated by {@code calculateArea()}.
     *
     * @param p1 Point p1
     * @param p2 Point p2
     * @param p3 Point p3
     * @return the area of the triangle formed by points {@code p1, p2, p3}.
     */
    double getArea(Point p1, Point p2, Point p3);

    /**
     * Prints the x and y coordinate of a variable number of Points.
     *
     * @param points variable number of points that are to be printed.
     */
    void printPoints(Point... points);
}
