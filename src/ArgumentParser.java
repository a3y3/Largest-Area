/**
 * Parses arguments passed in String args[] and modifies the passed LargestArea's
 * instance variables accordingly.
 *
 * @author Soham Dongargaonkar [sd4324] on 10/07/2019
 */
class ArgumentParser {
    private LargestArea largestArea;

    ArgumentParser(LargestArea largestArea) {
        this.largestArea = largestArea;
    }

    /**
     * Parses arguments and sets high and low values.
     *
     * @param args arguments from STDIN
     */
    void parseArguments(String[] args) {
        try {
            largestArea.numPoints = Integer.parseInt(args[0]);
            largestArea.side = Integer.parseInt(args[1]);
            largestArea.seed = Integer.parseInt(args[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Argument error.");
            System.err.println("Required arguments one, two are incorrect. See below " +
                    "for details. Exiting ...");
            displayHelp();
        }
        for (int i = 3; i < args.length; i++) {
            if (args[i].equals("-v")) {
                largestArea.verboseOutputs = true;
                break;      //For performance, MUST remove if adding more flags.
            } else if (args[i].equals(("-h"))) {
                displayHelp();
            }
        }
    }

    /**
     * Displays usages and quits the JVM. Be cautious; using this method will quit this
     * program, so use only if the arguments supplied are incorrect or if the user
     * wishes to see how to use the program by specifying the -h flag.
     */
    private void displayHelp() {
        System.out.println("USAGE - java LargestArea <num_points> <side> <seed>");
        System.exit(0);
    }
}
