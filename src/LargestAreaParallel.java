import mpi.*;

/**
 * Gets the Largest Area in parallel using OpenMPI.
 *
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestAreaParallel extends LargestArea {
    private int subArrayLength;
    private Comm comm;
    private int rank;
    private int worldSize;

    private LargestAreaParallel() {
        try {
            comm = MPI.COMM_WORLD;
            rank = comm.getRank();
            worldSize = comm.getSize();
        } catch (MPIException m) {
            System.err.println("MPIException in constructor. Exiting..." + m);
            System.exit(1);
        }
    }

    private final static int TAG_TYPE_POINTS = 0;
    private final static int TAG_TYPE_RESULT = 1;

    /**
     * Runs the program. Please note that depending upon which node runs this code,
     * different parts of this code will be executed.
     * If master runs, then the {@code runProgram()} of the parent class is called.
     * The slave nodes call {@code getLargestArea} directly. It is important to note
     * the different functions in which the values of the function will be returned.
     *
     * @param args STDIN arguments.
     */
    public static void main(String[] args) {
        try {
            MPI.Init(args);

            LargestAreaParallel largestAreaParallel = new LargestAreaParallel();
            new ArgumentParser(largestAreaParallel).parseArguments(args);
            Point[] points = largestAreaParallel.getPoints();
            if (largestAreaParallel.rank == 0) {
                System.out.println("*** STARTING PARALLEL EXECUTION ***");
                largestAreaParallel.runProgram(points);
            } else {
                AreaResultHolder partialResult = largestAreaParallel.getLargestArea(points);
                largestAreaParallel.sendResultToMaster(partialResult);
            }
            MPI.Finalize();
        } catch (MPIException m) {
            System.err.println("MPI Exception occurred: " + m);
        }
    }

    /**
     * Fetch the largest area for these {@code points}. This method will get the largest
     * area in parallel using MPI.
     *
     * @param points the points for which the area is to be found.
     */
    @Override
    public AreaResultHolder getLargestArea(Point[] points) {
        if (rank != 0) {
            //Slave
            int numParts = points.length / worldSize;
            int startingIndex = rank * numParts;
            int endIndex = startingIndex + numParts - 1;
            if (verboseOutputs) {
                System.out.println("Slave Rank: " + rank + "start: " + startingIndex +
                        " end: " + endIndex);
            }
            return getLargestAreaSequentially(points, startingIndex, endIndex);
        }
        //Master
        int leftOverStartingIndex = getLeftOverPointsIndex(points);
        if (verboseOutputs) {
            System.out.println("MASTER: leftOverStartingIndex is " + leftOverStartingIndex);
        }
        AreaResultHolder[] results = getResults(points, leftOverStartingIndex);
        return getMaxResult(results);
    }

    /**
     * If the number of nodes does not divide the array exactly, some points will be
     * left out. The master takes the starting index of these indices and calculates
     * the area for the pair of points.
     *
     * @param points The group of points.
     * @return the startingIndex of the "leftover" points.
     */
    private int getLeftOverPointsIndex(Point[] points) {
        int numParts = points.length / worldSize;
        return worldSize * numParts;
    }

    /**
     * Run *only* by the master node. The method receives the partial results of all
     * the nodes and returns an array containing the results.
     *
     * @param points                the points array being worked upon.
     * @param leftOverStartingIndex the index of the left over points.
     *                              {@see getLeftOverPointsIndex()}.
     * @return the results.
     */
    private AreaResultHolder[] getResults(Point[] points, int leftOverStartingIndex) {
        AreaResultHolder[] results = new AreaResultHolder[worldSize];
        int resultsCounter = 0;
        if (points != null) {
            AreaResultHolder masterResult = getLargestAreaSequentially(points,
                    leftOverStartingIndex, points.length);
            results[resultsCounter++] = masterResult;
        }
        try {
            for (int i = 1; i < worldSize; i++) {
                double[] nodeResultArr = new double[AreaResultHolder.TOTAL_LENGTH];
                comm.recv(nodeResultArr, AreaResultHolder.TOTAL_LENGTH, MPI.DOUBLE,
                        MPI.ANY_SOURCE, TAG_TYPE_RESULT);
                AreaResultHolder nodeResult = AreaResultHolder.getHolder(nodeResultArr);
                if (verboseOutputs) {
                    System.out.println("MASTER: received result: ");
                    nodeResult.printMaxArea();
                }
                results[resultsCounter++] = nodeResult;
            }
        } catch (MPIException m) {
            System.err.println("MPI Exception occurred: in getResults()" + m);
        }
        return results;
    }

    /**
     * Called by a slave node. Once a result is found, this method sends it to the
     * master node.
     *
     * @param partialResult the found result.
     */
    private void sendResultToMaster(AreaResultHolder partialResult) {
        double[] resultArray = partialResult.getArray();
        try {
            comm.send(resultArray, resultArray.length, MPI.DOUBLE, 0, TAG_TYPE_RESULT);
        } catch (MPIException m) {
            System.err.println("MPI Exception occurred: " + m);
        }
    }

    /**
     * Loops over the results sent by the nodes and selects the highest result.
     *
     * @param results the results sent by the slaves.
     * @return the final result; that is, the triangle with the highest area.
     */
    private AreaResultHolder getMaxResult(AreaResultHolder[] results) {
        AreaResultHolder maxResult = results[0];
        for (int i = 1; i < results.length; i++) {
            AreaResultHolder newResult = results[i];
            if (updateMaxArea(maxResult, newResult)) {
                maxResult = newResult;
            }
        }
        return maxResult;
    }

    /**
     * Determines if the newer area should be replace the older area.
     *
     * @param oldMax the current result.
     * @param newMax the new result.
     * @return true iff the newer result should replace the old result.
     */
    private boolean updateMaxArea(AreaResultHolder oldMax, AreaResultHolder newMax) {
        if (oldMax.getMaxArea() < newMax.getMaxArea()) {
            return true;
        } else if (oldMax.getMaxArea() == newMax.getMaxArea()) {
            int[] oldIndices = oldMax.getMaxIndices();
            int[] newIndices = newMax.getMaxIndices();
            return (replaceWithNewerPoints(oldIndices, newIndices));
        }
        return false;
    }
}