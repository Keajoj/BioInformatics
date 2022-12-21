package cs321.search;

/**
 * Wrapper class for important information that is supplied to the GeneBankSearchBTree program.
 */
public class GeneBankSearchBTreeArguments
{
    private final boolean useCache;
    private final int degree;
    private final String bTreeFileName;
    private final int subsequenceLength;
    private final String queryFileName;
    private final int cacheSize;
    private final int debugLevel;

    /**
     * Constructor for GeneBankSearchBTreeArguments object.
     * @param useCache boolean, whether or not the BTree is utilizing a cache
     * @param degree int, the degree of the B-Tree
     * @param bTreeFileName String, the name of the datafile containing the B-Tree
     * @param subsequenceLength int, the length of DNA subsequences contained in the B-Tree
     * @param queryFileName String, the name of the query file containing search queries
     * @param cacheSize int, the size of the cache if a cache is being used
     * @param debugLevel int, represents the level of verbosity desired in Debug functions
     */
    public GeneBankSearchBTreeArguments(boolean useCache, int degree, String bTreeFileName, int subsequenceLength, String queryFileName, int cacheSize, int debugLevel)
    {
        this.useCache = useCache;
        this.degree = degree;
        this.bTreeFileName = bTreeFileName;
        this.subsequenceLength = subsequenceLength;
        this.queryFileName = queryFileName;
        this.cacheSize = cacheSize;
        this.debugLevel = debugLevel;
    }

    /**
     * Gets whether or not the B-Tree uses a cache.
     * @return true if using cache, false if not
     */
    public boolean isUsingCache() {
        return useCache;
    }

    /**
     * The degree of the B-Tree.
     * @return int degree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * The filename of the B-Tree data file
     * @return String bTreeFilename
     */
    public String getBTreeFileName() {
        return bTreeFileName;
    }

    /**
     * The length of DNA subsequences stored in the B-Tree
     * @return int subsequenceLength
     */
    public int getSubsequenceLength() {
        return subsequenceLength;
    }

    /**
     * The name of the query file containing search queries.
     * @return String queryFileName
     */
    public String getQueryFileName() { return queryFileName; }

    /**
     * The size of the cache that the BTree is using.
     * @return int cacheSize
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * The desired level of debug information to be written to standard out and standard error streams.
     * @return int debugLevel
     */
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
        GeneBankSearchBTreeArguments other = (GeneBankSearchBTreeArguments) obj;
        if (cacheSize != other.cacheSize)
        {
            return false;
        }
        if (debugLevel != other.debugLevel)
        {
            return false;
        }
        if (degree != other.degree)
        {
            return false;
        }
        if (bTreeFileName == null)
        {
            if (other.bTreeFileName != null)
            {
                return false;
            }
        }
        else
        {
            if (!bTreeFileName.equals(other.bTreeFileName))
            {
                return false;
            }
        }
        if (subsequenceLength != other.subsequenceLength)
        {
            return false;
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
        if (useCache != other.useCache)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        //this method was generated using an IDE
        return "GeneBankCreateBTreeArguments{" +
                "useCache=" + useCache +
                ", degree=" + degree +
                ", bTreeFileName='" + bTreeFileName + '\'' +
                ", subsequenceLength=" + subsequenceLength +
                ", queryFileName='" + queryFileName + '\'' +
                ", cacheSize=" + cacheSize +
                ", debugLevel=" + debugLevel +
                '}';
    }
}
