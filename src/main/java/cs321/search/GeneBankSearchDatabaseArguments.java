package cs321.search;

public class GeneBankSearchDatabaseArguments
{
    //String usageMessage = "Usage: java -jar build/libs/GeneBankSearchDatabase.jar <path_to_SQLite_database> " +
    //        "<query_file> [<debug_level>]";

    private final String sqliteDatabasePath;
    private final String queryFileName;
    private final int debugLevel;

    public GeneBankSearchDatabaseArguments(String sqliteDatabasePath, String queryFileName, int debugLevel)
    {
        this.sqliteDatabasePath = sqliteDatabasePath;
        this.queryFileName = queryFileName;
        this.debugLevel = debugLevel;
    }

    public String getSqliteDatabasePath() {
        return sqliteDatabasePath;
    }

    public String getQueryFileName() {
        return queryFileName;
    }

    public int getDebugLevel() {
        return debugLevel;
    }

    @Override
    public boolean equals(Object obj)
    {
        //this method was generated using an IDE
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        GeneBankSearchDatabaseArguments other = (GeneBankSearchDatabaseArguments) obj;
        if (debugLevel != other.debugLevel)
        {
            return false;
        }
        if (sqliteDatabasePath == null)
        {
            if (other.sqliteDatabasePath != null)
            {
                return false;
            }
        }
        else
        {
            if (!sqliteDatabasePath.equals(other.sqliteDatabasePath))
            {
                return false;
            }
        }
        if (queryFileName == null)
        {
            if (other.queryFileName != null)
            {
                return false;
            }
        }
        else
        {
            if (!queryFileName.equals(other.queryFileName))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        //this method was generated using an IDE
        return "GeneBankCreateBTreeArguments{" +
                ", sqliteDatabasePath='" + sqliteDatabasePath + '\'' +
                ", queryFileName='" + queryFileName + '\'' +
                ", debugLevel=" + debugLevel +
                '}';
    }

}
