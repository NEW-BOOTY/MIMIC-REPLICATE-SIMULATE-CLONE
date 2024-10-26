/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class ShorsAlgorithm {
    private int numberToFactor;
    private QuantumCircuit quantumCircuit;
    private int numberOfQubits;

    public ShorsAlgorithm(int numberToFactor) {
        if (numberToFactor <= 1) {
            throw new IllegalArgumentException("Number to factor must be greater than 1.");
        }
        this.numberToFactor = numberToFactor;
        this.numberOfQubits = (int) Math.ceil(Math.log(numberToFactor) / Math.log(2));
        this.quantumCircuit = new QuantumCircuit(numberOfQubits);
    }

    public void initialize() {
        int randomBase = (int) (Math.random() * (numberToFactor - 2)) + 2;
        quantumCircuit.addGate(new QuantumModularExponentiation(randomBase, 1, numberToFactor));
        quantumCircuit.addGate(new PhaseEstimation(numberOfQubits, new QuantumModularExponentiation(randomBase, 1, numberToFactor)));
    }

    public int execute() {
        double[] stateVector = new double[1 << numberOfQubits];
        for (int i = 0; i < stateVector.length; i++) {
            stateVector[i] = 1.0 / Math.sqrt(stateVector.length);
        }

        quantumCircuit.execute(stateVector);
        int measuredValue = quantumCircuit.measure(stateVector);

        return ClassicalPostProcessing.findPeriod(measuredValue, numberOfQubits);
    }

    public int getNumberToFactor() {
        return numberToFactor;
    }
}
