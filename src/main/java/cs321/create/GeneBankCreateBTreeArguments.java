package cs321.create;

public class GeneBankCreateBTreeArguments
{
    private final boolean useCache;
    private final int degree;
    private final String gbkFileName;
    private final int subsequenceLength;
    private final int cacheSize;
    private final int debugLevel;

    public GeneBankCreateBTreeArguments(boolean useCache, int degree, String gbkFileName, int subsequenceLength, int cacheSize, int debugLevel)
    {
        this.useCache = useCache;
        this.degree = degree;
        this.gbkFileName = gbkFileName;
        this.subsequenceLength = subsequenceLength;
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
     * The filename of the .gbk file to be read into the B-Tree.
     * @return String gbkFileName
     */
    public String getGbkFileName() {
        return gbkFileName;
    }

    /**
     * The length of DNA subsequences stored in the B-Tree
     * @return int subsequenceLength
     */
    public int getSubsequenceLength() {
        return subsequenceLength;
    }

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
        GeneBankCreateBTreeArguments other = (GeneBankCreateBTreeArguments) obj;
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
        if (gbkFileName == null)
        {
            if (other.gbkFileName != null)
            {
                return false;
            }
        }
        else
        {
            if (!gbkFileName.equals(other.gbkFileName))
            {
                return false;
            }
        }
        if (subsequenceLength != other.subsequenceLength)
        {
            return false;
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
                ", gbkFileName='" + gbkFileName + '\'' +
                ", subsequenceLength=" + subsequenceLength +
                ", cacheSize=" + cacheSize +
                ", debugLevel=" + debugLevel +
                '}';
    }
}
