package cs321.btree;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cs321.create.GeneBankCreateBTree;
import cs321.create.GeneBankCreateBTreeArguments;

public class BTreeTest
{
    // HINT:
    //  instead of checking all intermediate states of constructing a tree
    //  you can check the final state of the tree and
    //  assert that the constructed tree has the expected number of nodes and
    //  assert that some (or all) of the nodes have the expected values
    @Test
    public void btreeDegree4Test()
    {
//        //TODO instantiate and populate a bTree object
//        int expectedNumberOfNodes = TBD;
//
//        // it is expected that these nodes values will appear in the tree when
//        // using a level traversal (i.e., root, then level 1 from left to right, then
//        // level 2 from left to right, etc.)
//        String[] expectedNodesContent = new String[]{
//                "TBD, TBD",      //root content
//                "TBD",           //first child of root content
//                "TBD, TBD, TBD", //second child of root content
//        };
//
//        assertEquals(expectedNumberOfNodes, bTree.getNumberOfNodes());
//        for (int indexNode = 0; indexNode < expectedNumberOfNodes; indexNode++)
//        {
//            // root has indexNode=0,
//            // first child of root has indexNode=1,
//            // second child of root has indexNode=2, and so on.
//            assertEquals(expectedNodesContent[indexNode], bTree.getArrayOfNodeContentsForNodeIndex(indexNode).toString());
//        }
    }

    @Test 
    public void bTreeTest() {
        GeneBankCreateBTreeArguments args = new GeneBankCreateBTreeArguments(false, 25, "data/files_gbk/hs_ref_chrY.gbk", 12, 0, 1);
        BTree t = new BTree<>(args);
        GeneBankCreateBTree.populateBTreeFromGeneBankFile(t, args);
        t.print();
        t.closeTree();
    }

    @Test
    public void bTestWithLevelOrder() {
        GeneBankCreateBTreeArguments args = new GeneBankCreateBTreeArguments(false, 2, "hs_ref_chrY.gbk", 12, 0, 1);

        BTree t = new BTree<>(args);
        
        for(int i = 0; i < 13; i++) {
            t.insert((long)i);
            assertEquals(String.valueOf((long)i), t.levelOrder(0).printKeys());
        }                   
        //test 1-13
        //assert in between each add   
    }

}
