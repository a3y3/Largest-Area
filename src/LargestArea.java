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
        RandomPoints r = new RandomPoints(largestArea.numPoints, largestArea.side,
                largestArea.seed);
    }
}
