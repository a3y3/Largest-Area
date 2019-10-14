import mpi.*;

/**
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestAreaParallel extends LargestArea {
    private int subArrayLength;
    private Comm comm = MPI.COMM_WORLD;
    private int rank = comm.getRank();
    private int worldSize = comm.getSize();

    private final static int TAG_TYPE_POINTS = 0;
    private final static int TAG_TYPE_RESULT = 1;
    public static void main(String[] args) {
        MPI.Init(args);

        LargestAreaParallel largestAreaParallel = new LargestAreaParallel();
        new ArgumentParser(largestAreaParallel).parseArguments(args);

        if (largestAreaParallel.rank == 0) {
            Point[] points = largestAreaParallel.getPoints();
            System.out.println("*** STARTING PARALLEL EXECUTION ***");
            largestAreaParallel.runProgram(points);
        } else {
            largestAreaParallel.getLargestArea(null);
        }
    }

    /**
     * Fetch the largest area for these {@code points}. This class will get the largest
     * area in parallel using MPI.
     *
     * @param points the points for which the area is to be found.
     */
    @Override
    public AreaResultHolder getLargestArea(Point[] points) {
        if (rank != 0){
            points = new Point[subArrayLength];
            comm.recv(points, subArrayLength, MPI.INT, MPI.ANY_SOURCE, TAG_TYPE_POINTS);
            if (verboseOutputs){
                System.out.println("Received array:");
                printPoints(points);
            }
        }
        else{

        }
        return null;
    }
}