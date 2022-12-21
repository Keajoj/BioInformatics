package cs321.search;

import cs321.btree.BTree;
import cs321.common.ParseArgumentException;
import cs321.common.ParseArgumentUtils;

public class GeneBankSearchDatabase
{

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello world from cs321.search.GeneBankSearchDatabase.main");

    }

    private static void printUsageAndExit(String errorMessage)
    {
        String usageMessage = "Usage: java -jar build/libs/GeneBankSearchDatabase.jar <path_to_SQLite_database> " +
                "<query_file> [<debug_level>]";
        System.out.println(usageMessage);
        System.exit(1);
    }

}
