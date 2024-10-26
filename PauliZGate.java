/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class PauliZGate extends QuantumGate {

    public PauliZGate() {
        super("Pauli-Z");
    }

    @Override
    public void apply(double[] stateVector) {
        // Implementation of Pauli-Z gate application
        int length = stateVector.length;
        for (int i = 0; i < length; i++) {
            if (Integer.bitCount(i) % 2 == 1) {
                stateVector[i] = -1 * stateVector[i];
            }
        }
    }
}
