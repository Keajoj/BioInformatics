package cs321.btree;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import cs321.common.Debug;

public class DiskReadWrite {
    
    private int nodeSize;
    private int degree;
    private long nextAddress = 0;
    private boolean useCache;
    private FileChannel file;
    private ByteBuffer buffer;
    private MetaData metaData;
    private Debug debug;
    private Cache cache;
    private String treeDataFile;
    
    public DiskReadWrite(String treeDataFile, MetaData metadata, boolean useCache, int cacheSize) {
        this.metaData = metadata;
        this.treeDataFile = treeDataFile;
        if (new File(treeDataFile + ".metadata").exists()) {
            readMetaData();
        }
        this.degree = this.metaData.getDegree();
        this.useCache = useCache;
        File DATAFILE = new File(treeDataFile);
        cache = new Cache<Long,BTreeNode>(cacheSize);

        nodeSize = new BTreeNode(degree).getObjectSize();
        buffer = ByteBuffer.allocateDirect(nodeSize);
        try {
            if(!DATAFILE.exists()) {
                DATAFILE.createNewFile();
                RandomAccessFile dataFile = new RandomAccessFile(DATAFILE, "rw");
                file = dataFile.getChannel();

            } else {
                RandomAccessFile dataFile = new RandomAccessFile(DATAFILE, "rw");
                file = dataFile.getChannel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a node with a proper address, but we dont write() since 
     * there is no useful data yet
     * @return BTN with address properly set
     */
    public BTreeNode allocateNode() {
        BTreeNode alloc = new BTreeNode(metaData.getDegree(), nextAddress);
        nextAddress += Long.valueOf(nodeSize);
        cache.put(alloc.getAddress(),alloc);
        return alloc;
    }

    /**
     * Saves Metadata of constructed tree
     */
    public void writeMetaData() {
        try {
            File metaFile = new File(treeDataFile + ".metadata");
            System.out.println(treeDataFile + ".metadata");
            metaFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(metaFile);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(this.metaData);
            out.flush();
            out.close();
        } catch (IOException ioe) {
            Debug.logError("Unable to write metadata file.");
            Debug.logError(ioe.toString());
            ioe.printStackTrace();
        }
    }

    /**
     * Reads Metadata of pre-constructed tree
     */
    public void readMetaData() {
        File metaFile = new File(treeDataFile + ".metadata");
        MetaData metadata;
        try {
            if (!metaFile.exists()) {
                throw new IOException(metaFile.getAbsolutePath() + " does not exist.");
            } else {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(metaFile));
                this.metaData = (MetaData)in.readObject();
                in.close();
            }
        } catch (IOException ioe) {
            Debug.logError("Unable to read metadata file.");
            Debug.logError(ioe.toString());
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            Debug.logError("Metadata file is not in correct format.");
            Debug.logError(cnfe.toString());
            cnfe.printStackTrace();
        }
    }

    /**
     * If cache is not being used, read from the disk directly.
     * Otherwise, read from the cache.
     * @param addr address of node to read. Must be a multiple of nodeSize
     * @return node either reconstructed from disk, or returned from cache.
     */
    public BTreeNode read(long addr){
        if (useCache && cache != null) {
            BTreeNode result = (BTreeNode)cache.get(addr);
            if (result != null) {
                return result;
            }
        }
        if(addr % nodeSize != 0) {
           debug.logError("address: " + addr + "positioned at invalid index");
        }
        try {
            file.position(addr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.clear(); //still has junk from previous calls

        try {
            file.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        buffer.flip();

        BTreeNode recreatedNode = new BTreeNode(degree);

        recreatedNode.setAddress(buffer.getLong());
        recreatedNode.setNumKeys(buffer.getInt());
        recreatedNode.setNumChildren(buffer.getInt());
        for(int i = 0; i < recreatedNode.getKeys().length; i++) {
            TreeObject thisObject = new TreeObject();
            thisObject.setFrequency(buffer.getInt());
            thisObject.setKey(buffer.getLong());
            recreatedNode.setKey(i, thisObject);
        }
        for(int i = 0; i < recreatedNode.getChildren().length; i++) {
            recreatedNode.setChild(i, buffer.getLong());
        }
        int isLeafB = buffer.getInt();
        recreatedNode.setLeaf(isLeafB == 1 ? true : false);
        if (useCache && cache != null) {
            cache.put(recreatedNode.getAddress(),recreatedNode);
        }
        return recreatedNode;
    }

    /**
     * Writes to cache if useCache is true, otherwise writes 
     * only to disk.
     * @param node to be overwritten with new information
     */
    public void write(BTreeNode node) {
        try {
            file.position(node.getAddress());
        } catch (IOException e) {
            debug.logError("Could not position channel on address: " + node.getAddress());
            e.printStackTrace();
        }
        buffer.clear();

        long address = node.getAddress();
        int numKeys = node.getNumKeys();
        int numChildren = node.getNumChildren();
        TreeObject[] keys = node.getKeys();
        long[] children = node.getChildren();
        int isLeaf = node.isLeaf() ? 1 : 0;

        buffer.putLong(address);
        int total = 0;
        total += Long.BYTES;
        buffer.putInt(numKeys);
        total += Integer.BYTES;
        buffer.putInt(numChildren);
        total += Integer.BYTES;
        for(int i = 0; i < keys.length; i++) {
            buffer.putInt(keys[i].getFrequency());
            total += Integer.BYTES;
            buffer.putLong(keys[i].getKey());
            total += Long.BYTES;
        }
        for(int i = 0; i < children.length; i++) {
            buffer.putLong(children[i]);
            total += Long.BYTES;
        }
        buffer.putInt(isLeaf);
        total += Integer.BYTES;
        buffer.flip();
        try {
            file.write(buffer);
        } catch (IOException e) {
            debug.logError("Could not write to byte buffer.");
            e.printStackTrace();
        }
        debug.log("wrote " + total + " bytes to disk");
        if (useCache && cache != null) {
            cache.put(node.getAddress(),node);
        }
    }

    public void finish(MetaData metadata) throws IOException {
        this.metaData = metadata;
        writeMetaData();
        file.close();
    }

    public void printCache() {
        cache.values().forEach(value -> {
            System.out.println(value.toString());
        });
    }
}
