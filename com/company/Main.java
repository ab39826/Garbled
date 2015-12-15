package com.company;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) {

        //first as a basic format, we would like to single-thread the process. Also, we would like to manually construct the function in terms of boolean gates in order to simplify the process.


        //EG: let the circuit be a1 AND b1 where p1 has input a1 into circuit and b1 has input b1 into circuit

        //then we can have a structure eg:

/*
        long lStartTime = System.currentTimeMillis();

        ObliviousTransfer ot = new ObliviousTransfer();

        try {
            ot.exchange();
        } catch (Exception name) {
            System.out.println("exception in ot occurred");
        }

        long lEndTime = System.currentTimeMillis();

        long difference = lEndTime - lStartTime;

        System.out.println("Elapsed milliseconds: " + difference);
*/


        //AsymmetricCipher test = new AsymmetricCipher();


        /*
        SymmetricCipher test = new SymmetricCipher();


        try{

            test.testAES();
        }
        catch (Exception e){
            System.out.println("EXCEPTION ENCOUNTERED");
        }

*/

        int numGates = 4095;
        int numThreads = 1;
        System.out.println("starting tests for " + numGates + " gates, " + numThreads + " threads");
        long lStartTime = System.currentTimeMillis();
        Conductor conductor = new Conductor(numGates,numThreads);



/*
        GateNode[] temp;

        for(int i = 0; i < conductor.garbledDone.length; i++){
            temp = conductor.garbler.gateTree.getLayer(i);

            System.out.println("Layer " + i );
            for(int j = 0; j<temp.length; j++){
                System.out.print(temp[j].type + ", ");

            }
            System.out.println("");


        }

*/

        conductor.conduct();

        long endTime = System.currentTimeMillis();

        System.out.println("Total time elapsed is " + (endTime -lStartTime));


        //Then we send to p2

        //we have oblivious tranfer protocol in order for p2 to realize it's own garbled inputs


        //finally we move into circuit computation on garbled circuit by p2

        //then we ensure that p1 gets back final result as well

    }
}
