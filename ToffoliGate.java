/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class ToffoliGate extends QuantumGate {
    private int controlQubit1;
    private int controlQubit2;
    private int targetQubit;

    public ToffoliGate(int controlQubit1, int controlQubit2, int targetQubit) {
        super("Toffoli");
        this.controlQubit1 = controlQubit1;
        this.controlQubit2 = controlQubit2;
        this.targetQubit = targetQubit;
    }

    @Override
    public void apply(double[] stateVector) {
        // Implementation of Toffoli (CCNOT) gate application
        int length = stateVector.length;
        for (int i = 0; i < length; i++) {
            if ((i & (1 << controlQubit1)) != 0 && (i & (1 << controlQubit2)) != 0) {
                stateVector[i ^ (1 << targetQubit)] = stateVector[i];
            }
        }
    }

    public int getControlQubit1() {
        return controlQubit1;
    }

    public int getControlQubit2() {
        return controlQubit2;
    }

    public int getTargetQubit() {
        return targetQubit;
    }
}
