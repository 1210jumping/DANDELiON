package com.example.myeducationapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.example.myeducationapp.Tree.RBTree;
import com.example.myeducationapp.Tree.RBTree.Colour;

import java.util.ArrayList;

public class RBTreeTest {
    RBTree<Integer> tree;

    @Before
    public void setUp() {
        tree = new RBTree<Integer>();
//        tree.insert(7,null);
//        tree.insert(5,null);
//        tree.insert(9,null);
        tree.insert(7);
        tree.insert(5);
        tree.insert(9);
    }

    @Test(timeout=1000)
    public void testInsert() {
        Assert.assertEquals("7 5 9", tree.preOrder());
        Assert.assertEquals("5 7 9", tree.inOrder());
        Assert.assertEquals("5 9 7", tree.postOrder());
        ArrayList<Integer>arrayList=new ArrayList<>();
        tree.toList(arrayList);
        ArrayList<Integer>integerArrayList=new ArrayList<>();
        integerArrayList.add(5);
        integerArrayList.add(7);
        integerArrayList.add(9);
        Assert.assertEquals(integerArrayList,arrayList);
    }

    @Test(timeout=1000)
    public void testInsertDuplicate() {
        //tree.insert(9,null);

        tree.insert(9);
        Assert.assertEquals("7 5 9 9", tree.preOrder());
    }

    @Test(timeout=1000)
    public void testNodeRed() {
        Assert.assertTrue(tree.search(9).colour == Colour.RED);
    }

    @Test(timeout=1000)
    public void testInsertNewNode() {

       // tree.insert(12,null);
        tree.insert(12);
        Assert.assertEquals("7 5 9 12", tree.preOrder());
    }

    @Test(timeout=1000)
    public void testDeleteNode() {

        //tree.insert(12,null);
        tree.delete(7);
        Assert.assertEquals("9 5", tree.preOrder());
    }



    @Test(timeout=1000)
    public void testNodeRedToBlack() {
        // tree.insert(12,null);
        tree.insert(12);
        Assert.assertTrue(tree.search(5).colour == Colour.BLACK && tree.search(9).colour == Colour.BLACK);
    }

    @Test(timeout=1000)
    public void testInsertNewNodeRed() {
        // tree.insert(12,null);
        tree.insert(12);
        Assert.assertTrue(tree.search(12).colour == Colour.RED);
    }

    @Test(timeout=1000)
    public void testInsertNewNodeNoColourChange() {
        // tree.insert(12,null);

//        tree.insert(8,null);
        tree.insert(12);
        tree.insert(8);
        Assert.assertTrue(tree.search(5).colour == Colour.BLACK
                && tree.search(9).colour == Colour.BLACK
                && tree.search(8).colour == Colour.RED
                && tree.search(12).colour == Colour.RED);
    }


    @Test(timeout=1000)
    public void testInsertRotateRight() {
//        tree.insert(12,null);
//        tree.insert(8,null);
//        tree.insert(11,null);
//        tree.insert(10,null);

        tree.insert(12);
        tree.insert(8);
        tree.insert(11);
        tree.insert(10);
        Assert.assertEquals("7 5 9 8 11 10 12", tree.preOrder());
        Assert.assertEquals("5 9 8 11 10 12 7", tree.postOrder());
        Assert.assertEquals("5 7 9 8 11 10 12", tree.inOrder());
    }

    @Test(timeout=1000)
    public void testInsertRotateRightRightLeft() {
//        tree.insert(12,null);
//        tree.insert(11,null);
        tree.insert(12);
        tree.insert(11);

        Assert.assertEquals("7 5 11 9 12", tree.preOrder());
    }

    @Test(timeout=1000)
    public void testSearch1() {
        Assert.assertTrue(tree.search(7).key == 7);
    }

    @Test(timeout=1000)
    public void testSearch2() {
        Assert.assertTrue(tree.search(5).key == 5);
    }

    @Test(timeout=1000)
    public void testSearch3() {
        Assert.assertNull(tree.search(-3));
    }

    @Test(timeout=1000)
    public void testSearch4() {
        Assert.assertNull(tree.search(26));
    }
}

