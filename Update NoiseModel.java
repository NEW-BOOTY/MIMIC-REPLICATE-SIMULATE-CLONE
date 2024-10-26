/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public abstract class NoiseModel {
    public abstract void apply(double[] stateVector);
}

class DepolarizingNoiseModel extends NoiseModel {
    private double probability;

    public DepolarizingNoiseModel(double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("Probability must be between 0 and 1.");
        }
        this.probability = probability;
    }

    @Override
    public void apply(double[] stateVector) {
        for (int i = 0; i < stateVector.length; i++) {
            stateVector[i] *= (1 - probability);
        }
    }
}
