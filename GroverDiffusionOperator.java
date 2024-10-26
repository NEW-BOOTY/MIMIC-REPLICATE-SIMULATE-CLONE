/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class GroverDiffusionOperator extends QuantumGate {

    public GroverDiffusionOperator() {
        super("GroverDiffusion");
    }

    @Override
    public void apply(double[] stateVector) {
        // Implementation of Grover diffusion operator
        int length = stateVector.length;
        double average = 0.0;

        for (int i = 0; i < length; i++) {
            average += stateVector[i];
        }
        average /= length;

        for (int i = 0; i < length; i++) {
            stateVector[i] = 2 * average - stateVector[i];
        }
    }
}
