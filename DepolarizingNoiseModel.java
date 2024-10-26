/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

import java.util.Random;

public class DepolarizingNoiseModel extends NoiseModel {
    private double probability;
    private Random random;

    public DepolarizingNoiseModel(double probability) {
        super("Depolarizing Noise");
        this.probability = probability;
        this.random = new Random();
    }

    @Override
    public void apply(double[] stateVector) {
        int length = stateVector.length;
        for (int i = 0; i < length; i++) {
            if (random.nextDouble() < probability) {
                stateVector[i] = 0.0;
            }
        }
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
