package cs321.btree;

import java.lang.instrument.Instrumentation;

import cs321.create.SequenceUtils;

public class TreeObject
{
    private long key;
    private int frequency;

    /**
     * Constructor for key that will actually sit in a node.
     * @param key Key this object will hold
     */

    public TreeObject(long key) {
        this.key = key;
        frequency = 1;
    }
    /**
     * Constructor for either TreeObject whose key is either
     * not known, or just for access to local variables.
     */
    public TreeObject() {
        this.key = -1;
        frequency = 0;
    }
    
    public long getKey() { return this.key; }

    public void setKey(long key) {this.key = key;}

    public void incrementFrequency() { frequency++; }

    public int getFrequency() { return frequency; }

    public void setFrequency(int frequency) { this.frequency = frequency; }
    @Override
    public boolean equals(Object other) {
        long otherKey;
        try {
            otherKey = ((TreeObject) other).getKey();
        } catch (Exception e) {
            // This should only fail if we try to compare to non-TreeObject objects, which we won't,
            // but assuming we ever do, obviously they aren't equal and this returns false
            return false;
        }
        return (this.key == otherKey);
    }
    
    /**
     * Returns the size of this object, independantly from the data being held
     * so that the number stays constant. 
     * @returns integer amount of bytes this object stores
     */
    public int getObjectSize() {
        return Long.BYTES + Integer.BYTES; 
    }

    public String toString(int sequenceLength) {
        return SequenceUtils.longToDNAString(key, sequenceLength) + " " + frequency;
    }
}
