/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class QuantumModularExponentiation extends QuantumGate {
    private int base;
    private int modulus;
    private int exponent;

    public QuantumModularExponentiation(int base, int exponent, int modulus) {
        super("Quantum Modular Exponentiation");
        this.base = base;
        this.exponent = exponent;
        this.modulus = modulus;
    }

    @Override
    public void apply(double[] stateVector) {
        int length = stateVector.length;
        double[] newStateVector = new double[length];

        for (int i = 0; i < length; i++) {
            int newIndex = (int) (Math.pow(base, exponent) * i % modulus);
            newStateVector[newIndex] = stateVector[i];
        }

        System.arraycopy(newStateVector, 0, stateVector, 0, length);
    }

    public int getBase() {
        return base;
    }

    public int getModulus() {
        return modulus;
    }

    public int getExponent() {
        return exponent;
    }
}
