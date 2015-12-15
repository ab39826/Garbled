package com.company;

/**
 * Created by user on 12/6/2015.
 */
public class EvaluateTree {
    EvaluateNode root;

    int index;
    EvaluateNode[] gateLayer;


    public EvaluateTree(int size){
        //create a balanced binary tree with size elements

        EvaluateNode[] eArray = new EvaluateNode[size];


        for(int i = 0; i<eArray.length; i++){
            eArray[i] = new EvaluateNode();
        }

        root = createTree(eArray, 0, size -1);

    }


    public EvaluateNode createTree(EvaluateNode[] gateArray, int start, int end){
        if(start > end){
            return null;
        }

        int mid = (start + end)/2;
        EvaluateNode rootNode = gateArray[mid];

        rootNode.leftChild = createTree(gateArray,start, mid -1);
        rootNode.rightChild = createTree(gateArray, mid + 1, end);
        return rootNode;
    }


    public EvaluateNode[] getLayer(int n){

        index = 0;
        Double numElements = Math.pow(2, n);
        gateLayer = new EvaluateNode[numElements.intValue()];
        addKDistant(root, n);

        return gateLayer;
    }

    public void addKDistant(EvaluateNode node, int k){

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
