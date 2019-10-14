import mpi.*;

/**
 * @author Soham Dongargaonkar [sd4324] on 07/10/19
 */
public class LargestAreaParallel extends LargestArea {
    private int subArrayLength;
    private Comm comm;
    private int rank;
    private int worldSize;

    private LargestAreaParallel(){
        try {
            comm = MPI.COMM_WORLD;
            rank = comm.getRank();
            worldSize = comm.getSize();
        }catch (MPIException m){
            System.err.println("MPIException in constructor. Exiting..." + m);
            System.exit(1);
        }
    }

    private final static int TAG_TYPE_POINTS = 0;
    private final static int TAG_TYPE_RESULT = 1;

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
        }catch (MPIException m){
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
    public AreaResultHolder getLargestArea(Point[] points){
        if (rank != 0) {
            //Slave
            int numParts = points.length / worldSize;
            int startingIndex = rank * numParts;
            int endIndex = startingIndex + numParts - 1;
            if (verboseOutputs){
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

    private int getLeftOverPointsIndex(Point[] points) {
        int numParts = points.length / worldSize;
        return worldSize * numParts;
    }

    private AreaResultHolder[] getResults(Point[] points, int leftOverStartingIndex){
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
        }catch (MPIException m){
            System.err.println("MPI Exception occurred: in getResults()" + m);
        }
        return results;
    }

    private void sendResultToMaster(AreaResultHolder partialResult) {
        double[] resultArray = partialResult.getArray();
        try {
            comm.send(resultArray, resultArray.length, MPI.DOUBLE, 0, TAG_TYPE_RESULT);
        }catch (MPIException m){
            System.err.println("MPI Exception occurred: " + m);
        }
    }

    private AreaResultHolder getMaxResult(AreaResultHolder[] results) {
        AreaResultHolder maxResult = results[0];
        for (int i = 1; i < results.length; i++) {
            AreaResultHolder newResult = results[i];
            if (updateMaxArea(maxResult, newResult)){
                maxResult = newResult;
            }
        }
        return maxResult;
    }

    private boolean updateMaxArea(AreaResultHolder oldMax, AreaResultHolder newMax){
        if (oldMax.getMaxArea() < newMax.getMaxArea()){
            return true;
        }
        else if (oldMax.getMaxArea() == newMax.getMaxArea()){
            int[] oldIndices = oldMax.getMaxIndices();
            int[] newIndices = newMax.getMaxIndices();
            return (replaceWithNewerPoints(oldIndices, newIndices));
        }
        return false;
    }
}