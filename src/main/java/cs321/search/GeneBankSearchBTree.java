package cs321.search;

import cs321.btree.BTree;
import cs321.btree.BTreeNode;
import cs321.btree.MetaData;
import cs321.btree.TreeObject;
import cs321.common.Debug;
import cs321.common.ParseArgumentException;
import cs321.common.ParseArgumentUtils;
import cs321.create.GeneBankCreateBTreeArguments;
import cs321.create.SequenceUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

/**
 * This class searches a B-Tree that is already created and stored somewhere in disk. First, the data file
 * containing B-Tree information is loaded into memory, with only the root node being in memory at first. Some
 * important information is stored in a separate .metadata file which should be located in the same directory
 * as the B-Tree datafile. User must also supply a query file as a command line argument. This query file
 * contains DNA sequence data in String format, each sequence on a new line. This program parses this file line
 * by line and searches the B-Tree for the given sequence before printing the results to standard out.
 * <p>
 * Note: This assumes that the arguments provided contain the full name of the B-Tree datafile, including any
 * subdirectories it may be stored in inside of the main working directory. Without this information properly
 * provided, the B-Tree's metadata will not be loaded correctly and proper functioning of the program cannot
 * be guaranteed.
 */
public class GeneBankSearchBTree
{

    /**
     * Takes in a btreefile and loads a btree into memory. BTree files also have an associated *.metadata file
     * that will be loaded automatically and does not need to be provided by the user. User must also provide
     * a query file. This program then parses that query file and searches the BTree for occurrences of the
     * DNA sequences contained within the query before printing these results to the console.
     * @param args command line args
     */
    public static void main(String[] args)
    {
        GeneBankSearchBTreeArguments arguments = parseArgumentsAndHandleExceptions(args);
        Debug.init(arguments);

        File metaFile = new File(arguments.getBTreeFileName() + ".metadata");
        MetaData metadata = new MetaData(arguments.getDegree(),0,arguments.getDebugLevel(),new BTreeNode(arguments.getDegree()).getObjectSize(),0);
        try {
            if (!metaFile.exists()) {
                throw new IOException(metaFile.getAbsolutePath() + " does not exist.");
            } else {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(metaFile));
                metadata = (MetaData)in.readObject();
                in.close();
            }
        } catch (IOException ioe) {
            Debug.logError("Unable to read metadata file.");
            Debug.logError(ioe.toString());
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            Debug.logError("Metadata file is not in correct format.");
            Debug.logError(cnfe.toString());
            cnfe.printStackTrace();
        }

        File queryFile = new File(arguments.getQueryFileName());
        BTree bTree = new BTree(arguments,metadata);
        try {
            Scanner fScan = new Scanner(queryFile);
            while (fScan.hasNextLine()) {
                String query = fScan.nextLine();
                long sequence = SequenceUtils.DNAStringToLong(query);
                long complement = SequenceUtils.getComplement(sequence,arguments.getSubsequenceLength());
                int frequency = 0;
                TreeObject node = bTree.search(sequence);
                if (node != null) {
                    frequency += node.getFrequency();   // get frequency of the sequence
                }
                node = bTree.search(complement);
                if (node != null) {
                    frequency += node.getFrequency();   // add the frequency of its complement
                }
                System.out.println(query.toLowerCase() + " " + frequency);    // print results to console
            }
            fScan.close();
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
            ioe.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        Debug.exit();
    }

    /**
     * Parses command line arguments, confirms their validity, and handles any exceptions thrown due to
     * incorrectly provided arguments
     * @param args command line args
     * @return GeneBankSearchBTreeArguments, parsed and validated arguments
     */
    private static GeneBankSearchBTreeArguments parseArgumentsAndHandleExceptions(String[] args)
    {
        GeneBankSearchBTreeArguments geneBankSearchBTreeArguments = null;
        try
        {
            geneBankSearchBTreeArguments = parseArguments(args);
        }
        catch (ParseArgumentException e)
        {
            printUsageAndExit(e.getMessage());
        }
        return geneBankSearchBTreeArguments;
    }

    /**
     * Prints usage message and exits the program.
     * @param errorMessage
     */
    private static void printUsageAndExit(String errorMessage)
    {
        String usageMessage = "Usage: java -jar build/libs/GeneBankSearchBTree.jar --cache=<0/1> " +
                "--degree=<btree degree> --btreefile=<BTree file> --length=<sequence length> " +
                "--queryfile=<query file> [--cachesize=<n>] [--debug=0|1|2]";
        System.out.println(usageMessage);
        System.exit(1);
    }

    /**
     * Parses arguments as provided in the command line
     * @param args command line args
     * @return GeneBankSearchBTreeArguments correctly parsed and validated
     * @throws ParseArgumentException
     */
    public static GeneBankSearchBTreeArguments parseArguments(String[] args) throws ParseArgumentException
    {
        //input variables to be (validated and) converted into GeneBankCreateBTreeArguments obj
        Boolean useCache = null; //needs to be nether true nor false to check if value was set later
        String gbkFileName = "", 
            queryFileName = "", 
            bTreeFileName = "";
        int degree = 0, 
            subsequenceLength = 0, 
            cacheSize = 0, 
            debugLevel = 0;

        String[] validFlags = {
            "cache",
            "degree",
            "gbkfile",
            "length",
            "cachesize",
            "debug", 
            "btreefile", 
            "queryfile"
        };

        for(String thisArg : args) {
                thisArg = thisArg.replaceAll("--", "");
                int eqIndex = thisArg.indexOf("=");
                String argName = thisArg.substring(0, eqIndex);
                String argValue = thisArg.substring(eqIndex+1);

                boolean validFlag = false;
                for(int i = 0; i < validFlags.length; i++) {
                    if(argName.equals(validFlags[i])) {
                        validFlag = true;
                    }
                }
                if(!validFlag) {
                    throw new ParseArgumentException("Flag: " + thisArg + " is not a valid flag.");
                } else {//if thisArg is a valid arg, turn it into respective variable
                    switch (argName) {
                        case "cache":
                            int useCacheInt = ParseArgumentUtils.convertStringToInt(argValue);
                            ParseArgumentUtils.verifyRanges(useCacheInt, 0, 1);

                            if(useCacheInt == 0 || useCacheInt == 1) {
                                useCache = useCacheInt == 1 ? true : false;
                            } else {
                                throw new ParseArgumentException("useCache argument must be either 1 or 0");
                            }
                            break;
                        case "degree":
                            degree = ParseArgumentUtils.convertStringToInt(argValue);
                            //need to verifyRanges(degree, a, b), but idk what a, b are
                            break;
                        case "gbkfile":
                            //handle valid file check in createBTreeArguments
                            gbkFileName = argValue;
                            break;
                        case "queryfile":
                            queryFileName = argValue;
                            break;
                        case "btreefile":
                            bTreeFileName = argValue;
                            break;
                        case "length":
                            subsequenceLength = ParseArgumentUtils.convertStringToInt(argValue);
                            ParseArgumentUtils.verifyRanges(subsequenceLength, 1, 31);
                            break;
                        case "cachesize":
                            if(useCache){
                                cacheSize = ParseArgumentUtils.convertStringToInt(argValue);
                                ParseArgumentUtils.verifyRanges(cacheSize, 100, 500);
                            } else {
                                throw new ParseArgumentException("Cache Size specified, but <cache> set to false.");
                            }
                            break;
                        case "debug":
                            debugLevel = ParseArgumentUtils.convertStringToInt(argValue);
                            ParseArgumentUtils.verifyRanges(debugLevel, 0, 2);
                            break;
                    }
                }
        }

            //set default values if necessary / check for required args
            if(useCache == null) {
                throw new ParseArgumentException("Required argument --cache not specified.");
            }
            if(useCache && cacheSize == 0) {
                throw new ParseArgumentException("Using cache, but no --cachesize specified.");
            }
            //btreefile and queryfile dont have extensions (regular files), cant check here
            if(degree == 0){
                degree = ParseArgumentUtils.findOptimalDegree();
            }
            //debugLevel already set to default 0

        //Debug.log("Arguments validated! Searching BTree...");
        return new GeneBankSearchBTreeArguments(useCache, degree, bTreeFileName, subsequenceLength, queryFileName, cacheSize, debugLevel);
    }

}

