package cs321.create;

import cs321.common.ParseArgumentException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GeneBankCreateBTreeTest
{
    private String[] args;
    private GeneBankCreateBTreeArguments expectedConfiguration;
    private GeneBankCreateBTreeArguments actualConfiguration;

    @Test
    public void parseArgsWithCache() throws ParseArgumentException
    {
        args = new String[] {"--cache=1",  "--degree=3", "--gbkfile=hs_ref_chrY.gbk", "--length=5", "--cachesize=200"};

        expectedConfiguration = new GeneBankCreateBTreeArguments(true, 3, "hs_ref_chrY.gbk", 5, 200, 0);
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }
    @Test
    public void parseArgsWithoutCache() throws ParseArgumentException
    {
        args = new String[] {"--cache=0",  "--degree=25", "--gbkfile=hs_ref_chrY.gbk", "--length=12", "--debug=1"};

        expectedConfiguration = new GeneBankCreateBTreeArguments(false, 25, "hs_ref_chrY.gbk", 12, 0, 1);
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }
    
}
