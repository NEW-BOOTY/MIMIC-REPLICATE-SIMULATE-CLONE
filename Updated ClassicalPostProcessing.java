/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class ClassicalPostProcessing {

    public static int findPeriod(int measuredValue, int numberOfQubits) {
        if (measuredValue < 0 || numberOfQubits <= 0) {
            throw new IllegalArgumentException("Invalid measured value or number of qubits.");
        }

        double phase = measuredValue / Math.pow(2, numberOfQubits);
        return continuedFraction(phase, numberOfQubits);
    }

    private static int continuedFraction(double phase, int maxIterations) {
        int previousDenominator = 0;
        int currentDenominator = 1;
        double remainder = phase;

        for (int i = 0; i < maxIterations; i++) {
            int a = (int) Math.floor(remainder);
            int nextDenominator = a * currentDenominator + previousDenominator;
            double nextRemainder = 1.0 / (remainder - a);

            previousDenominator = currentDenominator;
            currentDenominator = nextDenominator;
            remainder = nextRemainder;

            if (Math.abs(remainder - Math.round(remainder)) < 1e-10) {
                break;
            }
        }

        return currentDenominator;
    }
}
