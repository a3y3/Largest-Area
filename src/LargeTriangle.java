public class LargeTriangle
{

    public static void main( String[] args ) {
        int numPoints = 100;
        RandomPoints rndPoints = new RandomPoints(numPoints, 100, 142857);
        int idx=1;
        Point p;
        while(rndPoints.hasNext()) {
            p = rndPoints.next();
            System.out.println(idx+": x=" + p.getX() + " y=" + p.getY());
            idx++;
        }
    }
}
