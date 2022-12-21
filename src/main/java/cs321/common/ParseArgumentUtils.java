package cs321.common;

import cs321.btree.*;
public class ParseArgumentUtils
{
    /**
     * Verifies if lowRangeInclusive <= argument <= highRangeInclusive
     */
    public static void verifyRanges(int argument, int lowRangeInclusive, int highRangeInclusive) throws ParseArgumentException
    {
        if (argument < lowRangeInclusive || argument > highRangeInclusive) {
            throw new ParseArgumentException("Argument not in valid range.");
        }
    }

    public static int convertStringToInt(String argument) throws ParseArgumentException
    {
        int converted;
        try {
            converted = Integer.parseInt(argument);
        } catch (NumberFormatException nfe){
            throw new ParseArgumentException(nfe.getMessage());
        }
        return converted;
    }
    
    /**
     * Uses the equation 4096 >= (size of key array)(2t-1) + (size of children array)(2t) + (size of fields other than keys and children in node)
     * which is then solved for t, and floored so that t <= the equation.
     * @returns optimal degree 
     */
    public static int findOptimalDegree() {
        int nodeFieldsSize = Long.BYTES + (Integer.BYTES*2);
        int treeObjectSize = new TreeObject().getObjectSize();
        
        return (int)Math.floor((4096 - nodeFieldsSize + treeObjectSize)/((Long.BYTES*2)+(treeObjectSize*2)));
    }
}
