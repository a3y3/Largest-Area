# Largest-Area
This repository holds a sequential and parallel program that finds the largest area of a triangle formed from random points.

## Example
### Sequential
```
java LargestAreaSeq 3000 100 142857
*** STARTING SERIAL EXECUTION ***
1576 0.0038473 79.791
2488 99.531 99.740
2923 96.984 0.14588
4930.8
Time taken for execution: 49643 ms
```
### Parallel
On a cluster of 3 nodes, the area is found 7 times faster:
```
mpiexec --hostfile /usr/local/pub/ph/TardisCluster3Nodes.txt --prefix /usr/local java -cp /usr/local/pub/ph/mpi.jar LargestAreaParallel 3000 100 142857
*** STARTING PARALLEL EXECUTION ***
1576 0.0038473 79.791
2488 99.531 99.740
2923 96.984 0.14588
4930.8
Time taken for execution: 7783 ms
```
## Problem definition
Given a group of two-dimensional points, we want to find the largest triangle, namely three distinct points that are the vertices of a triangle with the largest area.

The program must print four lines of output.The first line contains the index of the largest triangle's first vertex point, a space character, the X coordinate of the first vertex point, a space character, the Y coordinate of the first vertex point, and a newline. The line must be printed with this statement:

    System.out.printf ("%d %.5g %.5g%n", index, x, y);
    
The program must print the triangle with the largest area. If more than one triangle has the same largest area, the program must print the triangle with the smallest first index. If more than one triangle has the same largest area and smallest first index, the program must print the triangle with the smallest second index. If more than one triangle has the same largest area and smallest first index and smallest second index, the program must print the triangle with the smallest third index.

## Working
### Sequential
The approach used is brute force. 3 nested for loops generate all possible pairs for 3 points. The area is calculated and the maxArea is updated accordingly.
### Parallel
- The array of points is chopped up depending on the number of nodes.
- Each node already has its copy of the array. The array is not sent to each node as generating the array is trivial and the overhead of sending it through the network is not worth it, given the performance degradation.
- Each node works on its slot of the array.
- Each result found is sent to the master node using `MPI.send()`.
- The results are gathered by the master node (using `MPI.recv()`) and the largest area is found from the results.

## How to Build
The sequential program can be run directly using javac. Type `java java LargestAreaSeq 3000 100 142857` to run.

Running the parallel version is trickier. You will need:
- Access to a cluster.
- OpenMPI wrappers for Java. [This](https://blogs.cisco.com/performance/java-bindings-for-open-mpi) is a great place to start.
