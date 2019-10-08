/**
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestArea {
    int numPoints;
    int side;
    int seed;
    boolean verboseOutputs;
    public static void main(String[] args) {
        RandomPoints r = new RandomPoints(100, 100, 142857);
        while (r.hasNext()){
            Point p = r.next();
            System.out.println(p.getX() + ", " + p.getY());
        }
    }
}
