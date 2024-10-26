/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class CNOTGate extends QuantumGate {
    private int controlQubit;
    private int targetQubit;

    public CNOTGate(int controlQubit, int targetQubit) {
        super("CNOT");
        this.controlQubit = controlQubit;
        this.targetQubit = targetQubit;
    }

    @Override
    public void apply(double[] stateVector) {
        // Implementation of CNOT gate application
        // Specific implementation for CNOT gate
    }

    public int getControlQubit() {
        return controlQubit;
    }

    public int getTargetQubit() {
        return targetQubit;
    }
}
