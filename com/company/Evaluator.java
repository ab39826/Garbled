package com.company;

import javax.crypto.SecretKey;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *P2
 */
public class Evaluator {

    EvaluateTree evaluateTree;

    GarbledGate[][] garbledMatrix;
    boolean [] garbledDone;

    int[] inputBits;

    SecretKey[] garblerBits;
    SecretKey[] evaluatorBits;

    int numThreads;

    int finalVal;

    public Evaluator(GarbledGate[][] gMatrix, boolean [] gDone, int numT){

        garbledMatrix = gMatrix;
        garbledDone = gDone;
        numThreads = numT;
    }


    public void setInputBits(int[] input){
        inputBits = input;
    }


    public void initializeLayer(){
        EvaluateNode[] currentLayer = evaluateTree.getLayer(garbledMatrix.length-1);

        //multithread this,

        for(int index = 0; index < currentLayer.length; index++){
            currentLayer[index].inputTuple = new OTuple<SecretKey>(garblerBits[index],evaluatorBits[index]);
        }
    }

    public void generateCircuit(int size){

        evaluateTree = new EvaluateTree(size);
    }

    public void evaluateCircuit(){

        //EvaluateNode[] currentLayer = evaluateTree.getLayer(layer);

        //multithread process here to decrypt and set corresponding output key


        /*
        if(numThreads == 1){

            EvaluateNode[] eGates = evaluateTree.getLayer(garbledMatrix.length -1);
            setLayer(garbledMatrix[garbledMatrix.length - 1], 0);
            eGates[0].evaluateNode();


        }
        */

            AtomicInteger counter = new AtomicInteger(0);

            int currentLayer = garbledMatrix.length - 1;
            EvaluateThread[] eThreads;


            while (currentLayer >= 0) {

                while (garbledDone[currentLayer] == false) {
                    try {
                        System.out.println("Not done yet, try sleeping");
                        Thread.sleep(50);                 //1000 milliseconds is one second.
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                //now that it's read to be processed call setLayer to get GarbledGates into EvaluateTree

                setLayer(garbledMatrix[currentLayer], currentLayer);

                //then now we can multithread the evaulation process
                EvaluateNode[] eGates = evaluateTree.getLayer(currentLayer);

                eThreads = new EvaluateThread[numThreads];

                for (int i = 0; i < numThreads; i++) {
                    eThreads[i] = new EvaluateThread(eGates, counter);
                    eThreads[i].start();

                }

                for (int i = 0; i < numThreads; i++) {
                    try {
                        eThreads[i].join();
                    } catch (Exception e) {
                        System.out.println("eval Threads failed to end");
                    }
                }


                currentLayer--;


            }


    }

    class EvaluateThread extends Thread{

        EvaluateNode[] eNodes;
        AtomicInteger counter;


        public EvaluateThread(EvaluateNode[] eGates, AtomicInteger c){
            eNodes = eGates;
            counter = c;
        }

        public void run(){


            while(counter.get() < eNodes.length){
                int gateIndex = counter.getAndAdd(1);
                eNodes[gateIndex].evaluateNode();
            }


        }

    }



    public void setLayer(GarbledGate[] garbledValues, int layer){
        //set garbled members of evaluateTree nodes to corresponding garbledValues


        EvaluateNode[] currentLayer = evaluateTree.getLayer(layer);

        for(int index = 0; index < currentLayer.length; index++){
            currentLayer[index].gate = garbledValues[index];
        }
    }

}
