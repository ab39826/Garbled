package com.company;

import javax.crypto.SecretKey;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 11/7/2015.
 */
public class Garbler {

    GateTree gateTree;

    GarbledGate[][] garbledMatrix;

    boolean [] garbledDone;

    int[] inputBits;

    int numThreads;

    int finalVal;


    //should be able to take a basic gate node and garble it.
    // should be able to create basic nodes randomly
    //should be able to create binary tree out of it.
    //should be able to crete list of nodes at layer

    //should be able to update leafs appropriately with garbled inputs

    public Garbler (int size, GarbledGate[][] gMatrix, boolean[] gDone, int numT){
        //create basic circuit
        garbledMatrix = gMatrix;
        garbledDone = gDone;
        numThreads = numT;


    }

    public void setInputBits(int[] input){
        inputBits = input;
    }

    public void generateCircuit(int size){

        GateNode[] gateList = new GateNode[size];


        Random rand = new Random();
        //first randomly create gates within circuit
        for(int i = 0; i < gateList.length; i++){

            int randomInt = rand.nextInt(3);


            switch(randomInt){

                case GateValues.AND:
                    gateList[i] = new GateNode(GateValues.AND_GATE, "and");
                    break;
                case GateValues.OR:
                    gateList[i] = new GateNode(GateValues.OR_GATE, "or");
                    break;
                case GateValues.XOR:
                    gateList[i] = new GateNode(GateValues.XOR_GATE, "xor");
                    break;


            }
            //System.out.println("Gate is " + gateList[i].type);
        }

        //now we have array of gates, need to create binary tree out of this

        gateTree = new GateTree(gateList);

    }

    public GateTree[] generateCircuitCutNChoose(int size, boolean[] verification, int m){

        GateTree[] circuits = new GateTree[m];

        for(int i = 0; i< circuits.length; i++){
            generateCircuit(size);
            circuits[i] = gateTree;
        }
        GateTree[] verifier = new GateTree[m-1];

        int index = 0;
        for(int i = 0; i< verification.length; i++){
            if(verification[i] == true){
                verifier[index] = circuits[i];
                index++;
            }

        }

    return verifier;

    }

    public SecretKey[] sendCommittments(){

       GateNode[] obscuredInputs =  gateTree.getLayer(garbledMatrix.length -1);
        SecretKey[] commitments = new SecretKey[obscuredInputs.length];

        for(int i = 0; i< obscuredInputs.length; i++){
            if(inputBits[i] == 0) {
                commitments[i] = obscuredInputs[i].leftChild.keyTuple.zero;
            }
            else{
                commitments[i] = obscuredInputs[i].leftChild.keyTuple.one;
            }
        }
        return commitments;
    }

    public void initializeLayer(){
        GateNode[] currentLayer = gateTree.getLayer(garbledMatrix.length-1);

        //multithread this,


        for(int index = 0; index < currentLayer.length; index++){
            currentLayer[index].initializeInputKeys();
        }
    }

    public void garbleLayer(int layer){

        //process specified layer of circuits, compute keyTT/ generate garbledGates for all values

        GateNode[] currentLayer = gateTree.getLayer(layer);
        GarbledGate[] currentProcessedLayer = new GarbledGate[currentLayer.length];


        //multithread the processing here



        garbledMatrix[layer] = currentProcessedLayer;

        //now process all nodes in current layer and correspondingly update currentProcessedLayer indexes

    }

    //generate array of input keys for roles: ge
    public SecretKey[] obliviousTransfer(int[] incomingBits, boolean isG){

        GateNode[] currentLayer = gateTree.getLayer(garbledMatrix.length-1);
        SecretKey[] inputKeys = new SecretKey[incomingBits.length];


         //don't worry about multithreading this until the end maybe
        for(int i = 0; i< inputKeys.length; i++){
            if(incomingBits[i] == 0){
                inputKeys[i] = (isG) ? currentLayer[i].leftChild.keyTuple.zero : currentLayer[i].rightChild.keyTuple.zero;
            }
            else{
                inputKeys[i] = (isG) ? currentLayer[i].leftChild.keyTuple.one : currentLayer[i].rightChild.keyTuple.one;
            }
        }


        return inputKeys;

    }


    public int getFinalVal(SecretKey finalkey){

        if(gateTree.root.keyTuple.zero.hashCode() == finalkey.hashCode()){
            finalVal = 0;
            return 0;
        }
        else{
            finalVal = 1;
            return 1;
        }
    }


    public void startGarble(){



            //garble and set it done here

        if(false){
            GateNode[] g = gateTree.getLayer(garbledMatrix.length-1);
            g[0].generateOutputKeys();
            garbledMatrix[0][0].garbledList = g[0].generateGarbled();
            garbledMatrix[0][0].hashTuple = g[0].generateOutputHash();
            //garbledDone[0] = true;
        }
        else{
            supremeGarblerThead gT = new supremeGarblerThead();
            gT.start();
        }

    }

    class supremeGarblerThead extends Thread{

        AtomicInteger counter;

        public supremeGarblerThead(){
            counter = new AtomicInteger(0);
        }

        public void run(){
            GarblerThread[] gThreads;

            int currentLayer = garbledMatrix.length -1;
            while(currentLayer >=0){

            gThreads = new GarblerThread[numThreads];
            GateNode[] currentGates = gateTree.getLayer(currentLayer);


            for(int i=0;i<numThreads;i++){
                gThreads[i] = new GarblerThread(currentGates, counter, currentLayer);
                gThreads[i].start();

            }

                for(int i = 0; i<numThreads; i++){
                    try{
                        gThreads[i].join();
                    }
                    catch(Exception e){
                        System.out.println("garbler Threads failed to end");
                    }
                }

                GarbledGate[][] gUnit = garbledMatrix;
                garbledDone[currentLayer] = true;
                currentLayer = currentLayer -1;
                counter = new AtomicInteger(0);


            }

            /*
            for(int i = garbledDone.length-1; i>=0; i--){
                garbledDone[i] = true;
            }
            */
        }


    }

    class GarblerThread extends Thread{

        AtomicInteger counter;
        int maxIndex;
        int layer;
        GateNode[] gNodes;

        public GarblerThread(GateNode[] gNode, AtomicInteger c,  int l){
            counter = c;
            layer = l;
            maxIndex = (int) (Math.pow((double) 2, (double) (layer)));
            gNodes = gNode;
        }

        //termination condition. index of synchronized counter is at length fo processed layer.
        public void run(){

            //need to geenrate garble on gateNode as well as outputhash and update the appropriate elemnt in garbledMatrix
            while(counter.get() < maxIndex){
                int gateIndex = counter.getAndAdd(1);
                gNodes[gateIndex].generateOutputKeys();
                garbledMatrix[layer][gateIndex].garbledList = gNodes[gateIndex].generateGarbled();
                garbledMatrix[layer][gateIndex].hashTuple = gNodes[gateIndex].generateOutputHash();
            }


        }



    }

}
