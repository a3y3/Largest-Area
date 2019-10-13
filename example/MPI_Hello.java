import mpi.MPI;
import mpi.MPIException;

public class MPI_Hello {
    public static void main(String args[]) throws MPIException {
	System.out.println("Starting");
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.getRank(), size = MPI.COMM_WORLD.getSize();

        String hostname = MPI.getProcessorName();
        System.out.println("From Java Program: Number of tasks= "+size+", My rank="+rank+", Running on "+hostname);

        MPI.Finalize();
	System.out.println("Done");
    }
}
