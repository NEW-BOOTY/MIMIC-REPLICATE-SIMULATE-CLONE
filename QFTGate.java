/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class QFTGate extends QuantumGate {
    private int numberOfQubits;

    public QFTGate(int numberOfQubits) {
        super("QFT");
        this.numberOfQubits = numberOfQubits;
    }

    @Override
    public void apply(double[] stateVector) {
        int length = stateVector.length;
        double[] newStateVector = new double[length];

        for (int k = 0; k < length; k++) {
            newStateVector[k] = 0;
            for (int j = 0; j < length; j++) {
                newStateVector[k] += stateVector[j] * Math.exp(2 * Math.PI * 1j * k * j / length);
            }
            newStateVector[k] /= Math.sqrt(length);
        }

        System.arraycopy(newStateVector, 0, stateVector, 0, length);
    }

    public int getNumberOfQubits() {
        return numberOfQubits;
    }
}
