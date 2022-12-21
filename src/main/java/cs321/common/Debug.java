package cs321.common;

import cs321.create.GeneBankCreateBTreeArguments;
import cs321.search.GeneBankSearchBTreeArguments;

import java.io.*;

/**
 * Utility class providing a suite of functions useful for debugging. Though there is a constructor
 * that may be useful for initializing certain variables, this class contains mostly static methods
 * that are intended to be used by themselves in their static form.
 * <p>
 * Before using the static methods, the user must first call Debug.init(args) with args being either
 * GeneBankCreateBTreeArguments or GeneBankSearchBTreeArguments type. Because this class uses a
 * BufferedWriter to write to a dumpfile, the user must also call Debut.exit() before the program
 * finishes execution. This is required in order to save the information written to the dumpfile.
 * <p>
 * In general, a higher debug level results in more information being saved and printed throughout
 * program execution.
 */
public class Debug {

    private static int localDebugLevel;
    private static final String dumpFilename = "DUMP";
    private PrintWriter pw;
    private static BufferedWriter dumpWriter;
    private static boolean initialized;

    /**
     * Initializes Debug parameters. Creates a dumpfile and initializes a BufferedWriter to write to it.
     * If this method is called when Debug has already been initialized, it will call exit() first and
     * then re-run all initialization procedures.
     * @param args GeneBankCreateBTreeArguments, used to set debug level
     */
    public static void init(GeneBankCreateBTreeArguments args) {
        if (initialized) {
            // We really shouldn't be calling init() twice, but if we do, we just exit first
            exit();
        }
        initialized = true;
        setlocalDebugLevel(args.getDebugLevel());
        File dumpFile = new File(dumpFilename);
        try {
            dumpFile.createNewFile();
            dumpWriter = new BufferedWriter(new FileWriter(dumpFile));
        } catch (IOException ioe) {
            log("Could not write to file " + dumpFilename);
            log(ioe.toString());
        }
    }

    /**
     * Initializes Debug parameters. Creates a dumpfile and initializes a BufferedWriter to write to it.
     * If this method is called when Debug has already been initialized, it will call exit() first and
     * then re-run all initialization procedures.
     * @param args GeneBankSearchBTreeArguments, used to set debug level
     */
    public static void init(GeneBankSearchBTreeArguments args) {
        if (initialized) {
            exit();
        }
        initialized = true;
        setlocalDebugLevel(args.getDebugLevel());
        File dumpFile = new File(dumpFilename);
        try {
            dumpFile.createNewFile();
            dumpWriter = new BufferedWriter(new FileWriter(dumpFile));
        } catch (IOException ioe) {
            log("Could not write to file " + dumpFilename);
            log(ioe.toString());
        }
    }

    /**
     * Closes the BufferedWriter after writing its contents to the dumpfile. User is required to call Debug.init()
     * again in order to make further changes to the dumpfile.
     */
    public static void exit() {
        try {
            if (initialized) {
                dumpWriter.close();
                initialized = false;
            }
        } catch (Exception e) {
            log("Something went wrong while closing Debug.");
            log(e.toString());
        }
    }

    /**
     * Writes a string of text to the dumpfile. Each string will be printed to a new line in the dumpfile.
     * Only functions if debug level is set to 1.
     * @param message String to append to the dumpfile.
     */
    public static void dump(String message) {
        if (localDebugLevel == 1) {
            try {
                dumpWriter.write(message + "\n");
            } catch (IOException ioe) {
                log("Unable to write to dump file.");
                log(ioe.toString());
            }
        }
    }

    /**
     * Sets Debug's localDebugLevel, which affects the functioning of several internal methods.
     * @param localDebugLevel int representing the level of verbosity in Debug logging
     */
    public static void setlocalDebugLevel(int localDebugLevel) {
        Debug.localDebugLevel = localDebugLevel;
    }

    /**
     * Prints information directly to standard out. Only functions if debug level is set to 0.
     * @param message String to print to console
     */
    public static void log(String message) {
        if (localDebugLevel == 0) {
            System.out.println(message);
        }
    }

    /**
     * Prints information to standard error (System.err). Only functions if debug level is set to 0, otherwise
     * this messaging will be suppressed.
     * @param message String to print to error stream
     */
    public static void logError(String message) {
        if (localDebugLevel == 0) {
            System.err.println(message);
        }
    }

    /**
     * Constructor for Debug object. Not strictly necessary as most functions have been available through
     * static methods instead. Can be considered deprecated, and its usage is discouraged. However, it is left
     * included to ensure compatibility.
     * @param gbkFileName String representing .gbk file that is used to create BTree
     * @param debugLevel int representing level of debug verbosity
     */
    public Debug(String gbkFileName, int debugLevel) {
        setlocalDebugLevel(debugLevel);
        if (debugLevel == 1) {
            // Create a text file named "[gbkfilename].dump.txt"
            /* CHANGE THIS LATER TO JUST HAVE THE FILE NAME BE CALLED DUMP */
            File dumpFile = new File("results/dumpfiles " + gbkFileName + ".dump.txt");
            if (dumpFile.exists()) {
                dumpFile.delete();
            }
            try {
                dumpFile.createNewFile();
            } catch (IOException e) {
                Debug.logError("Unable to create dump file \"" + dumpFilename + "\": " + e.getMessage());
                return;
            }
            try {
                pw = new PrintWriter(dumpFile);
            } catch (Exception e) {
                Debug.logError("Unable to create PrintWriter \"" + dumpFilename + "\": " + e.getMessage());
            }
        }
    }

    /**
     * This method is primarily called by BTree.inOrderTreeWalk()
     * to append a key's toString output to the dump file.
     *
     */
    public void close() {
        if(pw != null) pw.close();
    }
}

