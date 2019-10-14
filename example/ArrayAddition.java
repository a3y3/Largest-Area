import mpi.*;

public class ArrayAddition {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        Comm comm = MPI.COMM_WORLD;
        int rank = comm.getRank();
        int worldSize = comm.getSize();

        ArrayAddition arrayAddition = new ArrayAddition();
        int numElements = Integer.parseInt(args[0]);
        int subArrayLength = numElements / worldSize;

        if (rank == 0) {
            // Master
            int[] array = arrayAddition.getArray(numElements);
            System.out.println("****INIT****");
            System.out.println("subArrayLength: " + subArrayLength);
            System.out.println("world size: " + worldSize);

            //Chop up the array into {@code worldSize} parts and send each part to a node

            int counter = 0;
            int rankCounter = 1;

            int[] subArray = new int[subArrayLength];
            int remainingElementsIndex = 0;
            int masterSum = 0;
            int finalResult = 0;

            for (int i = 0; i < array.length - subArrayLength; i++) {
                subArray[counter++] = array[i];
                if (counter == subArrayLength) {
                    //MPI_Send this array
                    System.out.println("MASTER: subArray is: ");
                    arrayAddition.printArray(subArray);
                    System.out.println("Sending array to " + rankCounter);
                    comm.send(subArray, subArrayLength, MPI.INT, rankCounter++, 0);
                    if (rankCounter >= worldSize) {
                        //Master will finish up the remaining elements
                        remainingElementsIndex = i + 1;
                        break;
                    }

                    //reset array for next node
                    subArray = new int[subArrayLength];
                    counter = 0;
                }
            }
            if (remainingElementsIndex != 0) {
                System.out.println("MASTER: remainingElementsIndex: " + remainingElementsIndex);
                for (int i = remainingElementsIndex; i < array.length; i++) {
                    masterSum += array[i];
                }
            }
            System.out.println("MASTER: Sum from " + remainingElementsIndex + " to " +
                    array.length + " is : " + masterSum);
            int sumOfPartials = 0;
            for (int i = 1; i < worldSize; i++) {
                int[] partialSum = new int[1];
                comm.recv(partialSum, 1, MPI.INT, MPI.ANY_SOURCE, 0);
                System.out.println("MASTER: received partial sum: " + partialSum[0]);
                sumOfPartials += partialSum[0];
            }
            System.out.println("MASTER: Sum of partials: " + sumOfPartials);
            finalResult = masterSum + sumOfPartials;
            System.out.println("******MASTER: FINISH******");
            System.out.println("Final recorded sum = " + finalResult);

        } else {
            //slave
            int[] subArray = new int[subArrayLength];
            comm.recv(subArray, subArrayLength, MPI.INT, MPI.ANY_SOURCE, 0);
            System.out.println("Received array:");
            arrayAddition.printArray(subArray);
            int[] sum = new int[]{0};
            for (int i = 0; i < subArrayLength; i++) {
                sum[0] += subArray[i];
            }
            System.out.println("Sum: " + sum[0] + "; Sending back to master");
            comm.send(sum, 1, MPI.INT, 0, 0);
        }

        MPI.Finalize();
    }

    private int[] getArray(int size) {
        int[] a = new int[size];
        for (int i = 1; i <= size; i++) {
            a[i - 1] = i;
        }
        return a;
    }

    private int[] getSlice(int[] array, int rank, int numElements) {
        int[] arr = new int[numElements];
        int startIndex = rank * numElements;
        int endIndex = rank * numElements + numElements - 1;
        if (endIndex + 1 - startIndex >= 0)
            System.arraycopy(array, startIndex, arr, 0, endIndex + 1 - startIndex);
        return arr;
    }

    private void printArray(int[] arr) {
        for (int a : arr) {
            System.out.print(a + ", ");
        }
        System.out.println();
    }
}
