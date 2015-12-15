package com.company;

import com.company.GateNode;

/**
 * Created by user on 12/5/2015.
 */
public class GateTree {
    GateNode root;
    int index;
    GateNode[] gateLayer;

    public GateTree(GateNode[] gateList){

        root = createTree(gateList, 0, gateList.length -1);

    }


    public GateNode[] getLayer(int n){

        index = 0;
        Double numElements = Math.pow(2, n);
        gateLayer = new GateNode[numElements.intValue()];
        addKDistant(root, n);

        return gateLayer;
    }



    public GateNode createTree(GateNode[] gateArray, int start, int end){
        if(start > end){
            return null;
        }

        int mid = (start + end)/2;
        GateNode rootNode = gateArray[mid];

        rootNode.leftChild = createTree(gateArray,start, mid -1);
        rootNode.rightChild = createTree(gateArray, mid + 1, end);
        return rootNode;
    }
    public void addKDistant(GateNode node, int k){

        if(node == null){
            return;
        }

        if(k == 0){
            gateLayer[index] = node;
            index++;
            return;
        }

        else{
            addKDistant(node.leftChild,k-1);
            addKDistant(node.rightChild,k-1);

        }


    }


}
