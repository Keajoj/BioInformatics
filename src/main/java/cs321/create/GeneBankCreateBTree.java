package cs321.create;

import java.io.*;
import java.util.Scanner;

import cs321.btree.BTree;
import cs321.common.Debug;
import cs321.common.ParseArgumentException;
import cs321.common.ParseArgumentUtils;

/**
 * This program reads a GeneBank file (.gbk) and parses its contained sequences for subsequences of a given
 * length. If a subsequence is encountered multiple times, the frequency of this subsequence is incremented.
 * This frequency updating is handled within the BTree class. This program will output varying levels of
 * process information depending on the debug level specified by the user. It will generate intermediate files
 * for help with data processing, and these files can be preserved after program exit by specifying a debug
 * level of 2.
 * <p>
 * Note that the BTree file created by this program will be stored in the same directory as the GeneBank file
 * that is used to populate the BTree in the first place. BTree metadata file will also be stored in this
 * same location. When performing automated tests via scripts, if GeneBank files are located in any directory
 * other than program root, user must take care to specify the additional directory information when providing
 * the filename. This will ensure proper loading of B-Tree information when using the created B-Tree in the
 * GeneBankSearchBTree program.
 */
public class GeneBankCreateBTree
{

    /**
     * Driver method for GeneBankCreateBTree. First validates user-provided arguments from the command line,
     * then creates a new B-Tree based off of these arguments. Afterward, this B-Tree will be populated with
     * DNA sequence information as contained in a .gbk file. Finally, the B-Tree will be completely saved to
     * disk. This program also times this process using System.currentTimeMillis() and prints the elapsed
     * time to standard output.
     * @param args command line arguments
     */
    public static void main(String[] args)
    {

            System.out.println("Parsing and validating arguments...");

            for (String arg : args) {
                System.out.println(arg);
            }
            GeneBankCreateBTreeArguments geneBankCreateBTreeArguments = parseArgumentsAndHandleExceptions(args);
            Debug.init(geneBankCreateBTreeArguments);

            BTree bTree = new BTree(geneBankCreateBTreeArguments);

        try {
            long start = System.currentTimeMillis();
            populateBTreeFromGeneBankFile(bTree, geneBankCreateBTreeArguments);
            if (geneBankCreateBTreeArguments.getDebugLevel() == 1) {
                bTree.print();
            }
            bTree.closeTree();
            long end = System.currentTimeMillis();
            System.out.println("Operation concluded successfully in " + (end - start) + " ms");
            Debug.exit();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * The final step of data processing. Parses the sequence (*.seq) file created by generateSequenceFile()
     * and iterates through each line. From that String, progressive substrings of length sequenceLength are
     * created, before being converted to a long value by SequenceUtils.DNAStringToLong() and ultimately
     * inserted into the BTree.
     * <p>
     * At the end of this process, the intermediate files (*.stripped, *.seq) are deleted, unless the user
     * has specified a debug level of 2. In that case, these files are preserved for inspection. However,
     * running this program again will overwrite any existing files that share these same names.
     * @param bTree BTree, the B-Tree to populate with sequence data
     * @param args GeneBankCreateBTreeArguments, the validated command-line arguments provided to the program
     */
    public static void populateBTreeFromGeneBankFile(BTree bTree, GeneBankCreateBTreeArguments args) {
        String gbkFilename = args.getGbkFileName();
        int seqLength = args.getSubsequenceLength();
        File strippedFile = generateStrippedFile(gbkFilename);
        File seqFile = generateSequenceFile(strippedFile, seqLength);
        try {
            Scanner fScan = new Scanner(seqFile);
            while (fScan.hasNextLine()) {
                String fullSeq = fScan.nextLine();
                for (int i = 0; i < fullSeq.length()-seqLength+1; i++) {
                    String subSeq = fullSeq.substring(i,(i+seqLength));

                    bTree.insert(SequenceUtils.DNAStringToLong(subSeq));
                    //bTree.print();
                }
            }
            fScan.close();
        } catch (IOException ioe) {
            System.out.println("Unable to read " + seqFile.getAbsolutePath());
            System.out.println(ioe.toString());
        }

        if (args.getDebugLevel() == 2) {
            // For debugLevel==2, we will save the *.stripped and *.seq files we created in intermediate steps
        } else {
            // Otherwise, we delete them now that they're no longer needed
            strippedFile.delete();
            seqFile.delete();
        }
    }

    /**
     * Intermediate step of .gbk file processing. Takes in a strippedFile, as returned by generateStrippedFile(),
     * and further formats the included text to remove all blank lines. This new file is titled "<gbkFilename>.seq"
     * (with "seq" being short for "sequence") and includes all DNA sequences from the .gbk file, all separated
     * by a new line for easy reading in a future step. Because the maximum length of a file line is equal to
     * Integer.MAX_VALUE, and we can verify that no .gbk files (that we will use, anyway) contain sequences
     * exceeding this size, we are properly preserving sequence data without truncating it.
     * <p>
     * This file is deleted in a later step of data processing, unless debug level is set to 2. In that case, the
     * file is preserved for user inspection. File is located in the same directory as the provided .gbk file.
     * @param strippedFile File, the stripped file generated by generatedStrippedFile()
     * @param seqLength int, the sequence length of DNA sequences stored in the B-Tree
     * @return File sequenceFile, the text file containing sequence data generated by this method
     */
    private static File generateSequenceFile(File strippedFile, int seqLength) {
        File sequenceFile = new File(strippedFile.getAbsolutePath().replace(".stripped",".seq"));
        try {
            sequenceFile.createNewFile();

            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(sequenceFile));
            Scanner fScan = new Scanner(strippedFile);
            while (fScan.hasNextLine()) {
                String nextLine = fScan.nextLine();
                if (nextLine.length() >= seqLength) {
                    fileWriter.write(nextLine);
                    if (fScan.hasNext()) {
                        fileWriter.write("\n");
                    }
                }
            }
            fScan.close();
            fileWriter.close();
        } catch (IOException ioe) {
            System.out.println("Unable to create " + sequenceFile.getName());
            System.out.println(ioe.toString());
        }
        return sequenceFile;
    }

    /**
     * Intermediate step of .gbk file processing. A .gbk file contains large amounts of header information and
     * additional text that is not necessary. This method takes a String with a .gbk file name and then parses
     * that file to locate only the relevant information, as marked by "ORIGIN" and "//" tags. This information
     * is then copied into a newly created file called "<gbkFilename>.stripped". When copying, this also removes
     * the internal spaces and character count at the beginning of each line, and replaces all occurrences of the
     * character 'n' with a newline ('\n') character.
     * <p>
     * This file is deleted in a later step of data processing, unless debug level is set to 2. In that case, the
     * file is preserved for user inspection. File is located in the same directory as the provided .gbk file.
     * @param gbkFilename String of .gbk file name
     * @return File representing the stripped text file
     */
    private static File generateStrippedFile(String gbkFilename) {
        String startSequenceKeyword = "ORIGIN";
        String endSequenceKeyword = "//";
        File gbkFile = new File(gbkFilename);
        File strippedFile = new File(gbkFile.getAbsolutePath() + ".stripped");

        try {
            strippedFile.createNewFile();

            FileWriter fw = new FileWriter(strippedFile);
            BufferedWriter fileWriter = new BufferedWriter(fw); //BufferedWriter is more efficient for large data
            Scanner fScan = new Scanner(gbkFile);

            boolean writeEnabled = false;
            while (fScan.hasNextLine()) {
                String nextLine = fScan.nextLine();
                if (nextLine.trim().equals(startSequenceKeyword)) {
                    writeEnabled = true;
                } else if (nextLine.trim().equals(endSequenceKeyword)) {
                    fileWriter.write("\n");
                    writeEnabled = false;
                } else if (writeEnabled) {
                    // Relevant data begins at index 10, then we strip out all whitespace, then write to file
                    nextLine = nextLine.substring(10)
                            .replaceAll("\\s","")
                            .replaceAll("n","\n");
                    fileWriter.write(nextLine);
                }
            }
            fileWriter.close();
            fScan.close();
        } catch (IOException ioe) {
            System.out.println("Unable to create " + strippedFile.getName());
            System.out.println(ioe.toString());
        }
        return strippedFile;
    }

    /**
     * Parses command line arguments, confirms their validity, and handles any exceptions thrown due to
     * incorrectly provided arguments
     * @param args command line args
     * @return GeneBankSearchBTreeArguments, parsed and validated arguments
     */
    private static GeneBankCreateBTreeArguments parseArgumentsAndHandleExceptions(String[] args)
    {
        GeneBankCreateBTreeArguments geneBankCreateBTreeArguments = null;
        try {
            geneBankCreateBTreeArguments = parseArguments(args);
        }
        catch (ParseArgumentException e) {
            printUsageAndExit(e.toString());
        }
        return geneBankCreateBTreeArguments;
    }

    /**
     * Prints usage message and exits the program.
     * @param errorMessage
     */
    private static void printUsageAndExit(String errorMessage)
    {
        System.out.println("Required arguments:\n " +
            "java -jar build/libs/GeneBankCreateBTree.jar --cache=<0|1>  --degree=<btree degree>  --gbkfile=<gbk file> --length=<sequence length> [--cachesize=<n>] [--debug=0|1|2|3]"
        );
        System.exit(1);
    }

    /**
     * Parses arguments as provided in the command line
     * @param args command line args
     * @return GeneBankSearchBTreeArguments correctly parsed and validated
     * @throws ParseArgumentException
     */
    public static GeneBankCreateBTreeArguments parseArguments(String[] args) throws ParseArgumentException
    {
        //input variables to be (validated and) converted into GeneBankCreateBTreeArguments obj
        Boolean useCache = null;
        int degree = 0;
        String gbkFileName = "";
        int subsequenceLength = 0;
        int cacheSize = 0;
        int debugLevel = 0;

        String[] validFlags = {
            "cache",
            "degree",
            "gbkfile",
            "length",
            "cachesize",
            "debug"
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
            if(!gbkFileName.substring(gbkFileName.length() - 3).equals("gbk")) {
                throw new ParseArgumentException("Given file is invalid file type, must be .gbk");
            }
            if(degree == 0){
                degree = ParseArgumentUtils.findOptimalDegree();
            }
            //debugLevel already set to default 0

        System.out.println("Arguments validated! Creating BTree...");
        return new GeneBankCreateBTreeArguments(useCache, degree, gbkFileName, subsequenceLength, cacheSize, debugLevel);
    }
}
