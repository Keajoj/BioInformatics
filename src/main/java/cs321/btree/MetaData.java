package cs321.btree;

import java.io.Serializable;

public class MetaData implements Serializable {
    private int degree;
    private int numNodes;
    private int nodeSize; 
    private long rootAddress;
    private int debugLevel;
    
    /**
     * Constructor for Metadata of a tree. 
     * @param degree degree of tree
     * @param numNodes number of nodes in tree
     * @param debugLevel neeed for appropriate logging
     * @param nodeSize 
     * @param address address of root node
     */
    public MetaData(int degree, int numNodes, int debugLevel, int nodeSize, long address) {
        this.rootAddress = address;
        this.degree = degree;
        this.numNodes = numNodes;
        this.nodeSize = nodeSize;
        this.debugLevel = debugLevel;
    }

    //exists just to get size of MetaData obj
    public MetaData(int degree) {

    }

    /* GETTERS AND SETTERS */
    public int getDegree() {
        return degree;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public long getRootAddress() {
        return rootAddress;
    }

    public int getDebugLevel() {
        return debugLevel;
    }

    public int getObjectSize() {
        return (Integer.BYTES * 4) + Long.BYTES;
    }

}
