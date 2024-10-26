/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class HadamardGate extends QuantumGate {

    public HadamardGate() {
        super("Hadamard");
    }

    @Override
    public void apply(double[] stateVector) {
        // Implementation of Hadamard gate application
        int length = stateVector.length;
        for (int i = 0; i < length; i++) {
            stateVector[i] = stateVector[i] / Math.sqrt(2);
        }
    }
}
