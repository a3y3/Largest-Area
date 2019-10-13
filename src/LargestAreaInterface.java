/**
 * @author Soham Dongargaonkar [sd4324] on 12/10/19
 */
public interface LargestAreaInterface {
    Point[] getPoints();

    void getLargestArea(Point[] points);

    double getLength(Point p1, Point p2);

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
