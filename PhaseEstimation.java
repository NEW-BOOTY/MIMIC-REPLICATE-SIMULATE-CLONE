/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class PhaseEstimation extends QuantumGate {
    private int numberOfQubits;
    private QuantumGate unitaryOperator;

    public PhaseEstimation(int numberOfQubits, QuantumGate unitaryOperator) {
        super("Phase Estimation");
        this.numberOfQubits = numberOfQubits;
        this.unitaryOperator = unitaryOperator;
    }

    @Override
    public void apply(double[] stateVector) {
        // Apply the inverse Quantum Fourier Transform
        QFTGate qftGate = new QFTGate(numberOfQubits);
        qftGate.apply(stateVector);

        // Apply the controlled unitary operator
        for (int i = 0; i < numberOfQubits; i++) {
            unitaryOperator.apply(stateVector);
        }

        // Apply the Quantum Fourier Transform
        qftGate.apply(stateVector);
    }

    public int getNumberOfQubits() {
        return numberOfQubits;
    }

    public QuantumGate getUnitaryOperator() {
        return unitaryOperator;
    }
}
