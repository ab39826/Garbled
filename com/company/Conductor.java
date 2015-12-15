package com.company;

import java.util.Random;

/**
 * Created by user on 12/7/2015.
 */
public class Conductor {
    Garbler garbler;
    Evaluator evaluator;

    GarbledGate [][] garbledMatrix;
    boolean [] garbledDone;
    int circuitSize;

    public Conductor(int size, int numThreads){

        Double levels = (double) Math.log(size + 1) / Math.log(2);
        int levelsint = levels.intValue();

        circuitSize = size;
        garbledMatrix = new GarbledGate[levelsint][(size+1)/2];

        for(int i =0; i <garbledMatrix.length; i++){

            for(int j =0; j<garbledMatrix[0].length; j++){
                garbledMatrix[i][j] = new GarbledGate();
            }
        }


        garbledDone = new boolean[levelsint];
        garbler = new Garbler(size, garbledMatrix, garbledDone, numThreads);
        evaluator = new Evaluator(garbledMatrix, garbledDone, numThreads);

    }



    public void conduct(){

        garbler.generateCircuit(circuitSize);
        evaluator.generateCircuit(circuitSize);

        //needs to garble all oblivious transfer bits.


        generateInputs();
        garbler.initializeLayer();
        ObliviousTransfer ot = new ObliviousTransfer();
        ot.transfer(garbler, evaluator);

        evaluator.initializeLayer();

        /////////////////////
        //Now can proceed with multithreading computational operations

        garbler.startGarble();
        evaluator.evaluateCircuit();

        //////////////
        //now finally exchange last key
        System.out.println("Done");
        //evaluator.finalVal = garbler.getFinalVal(evaluator.evaluateTree.root.outputKey);
        //System.out.println("Evaluator final: " + evaluator.finalVal);
        //System.out.println("Garbler final: " + garbler.finalVal);





    }

    public void generateInputs(){
        int size = garbledMatrix[0].length;
        int[] garblerInput = new int[size];
        int[] evaluatorInput = new int[size];


        Random rand = new Random();

        for(int index = 0; index < size; index++){
            garblerInput[index] = rand.nextInt(2);
            evaluatorInput[index] = rand.nextInt(2);
        }

        garbler.setInputBits(garblerInput);
        evaluator.setInputBits(evaluatorInput);


        System.out.println("Garbler Input: " + garblerInput[0]);
        System.out.println("Evaluator Input: " + evaluatorInput[0]);
    }


}
