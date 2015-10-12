package com.company;

public class Main {

    public static void main(String[] args) {

        //first as a basic format, we would like to single-thread the process. Also, we would like to manually construct the function in terms of boolean gates in order to simplify the process.


        //EG: let the circuit be a1 AND b1 where p1 has input a1 into circuit and b1 has input b1 into circuit

        //then we can have a structure eg:
        Wire a1=new Wire();
        Wire b1=new Wire();
        Wire result=new Wire(3);

        Gate g1=new Gate(a1,b1,result);


        //Then we send to p2

        //we have oblivious tranfer protocol in order for p2 to realize it's own garbled inputs


        //finally we move into circuit computation on garbled circuit by p2

        //then we ensure that p1 gets back final result as well

    }
}
