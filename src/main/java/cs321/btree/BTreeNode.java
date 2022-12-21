package cs321.btree;

import cs321.common.Debug;

public class BTreeNode {
       
    private long address;
    private int numKeys;
    private int numChildren;
    private boolean isLeaf;
    private TreeObject[] keys;
    private long[] children;

    /**
     * Constructor for a node created within DiskReadWrite, 
     * whos address is known to be valid and a multiple of 
     * nodeSize. `keys` and `children` must be initialized 
     * to have anything other than null, otherwise 
     * NullPointerExceptions are inevitable. 
     * @param degree Needed for creating correct sized array
     * @param address Where node will be stored in Disk
     */
    public BTreeNode (int degree, long address) {
        numKeys = 0;
        keys = new TreeObject[2*degree - 1];
        for(int i = 0; i < 2*degree-1; i++) {
            this.keys[i] = new TreeObject();
        }
        numChildren = 0;
        children = new long[2*degree];
        for(int i = 0; i < 2*degree; i++) {
            this.children[i] = 0;
        }
        this.address = address;
        Debug.log(String.valueOf(address));
    }
    /**
     * Creates a node that is not instantated with an address. 
     * This is useful if we want to create a dummy node, 
     * or a node whose proper address is not known at the
     * time of construction. 
     * @returns Node with minimal data
     */
    public BTreeNode(int degree) {
        keys = new TreeObject[2*degree - 1];
        for(int i = 0; i < 2*degree-1; i++) {
            this.keys[i] = new TreeObject();
        }
        children = new long[2*degree];
        for(int i = 0; i < 2*degree; i++) {
            this.children[i] = 0;
        }
    }
    /**
     * Calculates the amount of memory taken up by a single node object.
     * The amount is dependant solely on the degree.
     * @return Byte amount of memory used for one node as an integer
     */
    public int getObjectSize() {
        return ((keys.length) * new TreeObject(1).getObjectSize())  //size of keys (creates a dummy treeObject to access objectSize)
            + ((children.length * Long.BYTES) + Long.BYTES)      //size of children, address
            + (Integer.BYTES * 3) ;                  //degree, numKeys, numChildren
                                              //isLeaf
    }
    

    /* GETTERS AND SETTERS */
    public long getAddress() {
        return address;
    }
    public void setAddress(long addr) {
        this.address = addr;
    }

    public long getObjectsKey(int i) {
        return keys[i].getKey();
    }
    public void setObjectsKey(int i, long val) {
        keys[i].setKey(val);    
    }
    public TreeObject[] getKeys() {
        return this.keys;
    }
    public String printObjectsKeys() {
        String ret = "";
        for(TreeObject l : keys) {
            ret+=l.getKey() + " ";
        }
        return ret;
    }

    public int getNumChildren() {
        return numChildren;
    }
    public long[] getChildren() {
        return children;
    }
    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }
    public void incNumChildren() {
        this.numChildren++;
    }
    public long getChild(int i) {
		return children[i];
	}
    public void setChild(int i, BTreeNode child) {
        children[i] = child.getAddress();
    }
    public void setChild(int i, long address) {
        children[i] = address;
    }
    
    public int getNumKeys() {
        return numKeys;
    }
    public void setNumKeys(int newSize) {
        numKeys = newSize;
    }
    public void setKey(int i, TreeObject to) {
        keys[i] = to;
    }
    public TreeObject getKey(int i) {
        return keys[i];
    }

    public void decSize() {
        numKeys--;
    }
    public void incSize() {
        numKeys++;
    }
    public void incFrequency(int i) {
        keys[i].incrementFrequency();
    }

    public boolean isLeaf() {
        return isLeaf;
    }
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Address: " + address + "\n");
        sb.append("isLeaf: ");
        if (isLeaf) {
            sb.append("YES");
        } else {
            sb.append("NO");
        }
        sb.append("\n");
        sb.append("Children: (" + numChildren + ") [");
        for (long c : children) {
            sb.append(c);
            sb.append(", ");
        }
        sb.delete(sb.length()-2,sb.length());
        sb.append("]\n");
        sb.append("Keys: (" + numKeys + ") [");
        for (TreeObject t : keys) {
            sb.append(t.getKey());
            sb.append(", ");
        }
        sb.delete(sb.length()-2,sb.length());
        sb.append("]\n");
        return sb.toString();
    }

}
