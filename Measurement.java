/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

import java.util.Random;

public class Measurement {
    private Random random;

    public Measurement() {
        this.random = new Random();
    }

    public int measure(double[] stateVector) {
        double cumulativeProbability = 0.0;
        double randomValue = random.nextDouble();
        for (int i = 0; i < stateVector.length; i++) {
            cumulativeProbability += stateVector[i] * stateVector[i];
            if (randomValue < cumulativeProbability) {
                return i;
            }
        }
        return stateVector.length - 1; // Should not reach here if stateVector is normalized
    }
}
