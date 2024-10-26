/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class CustomQuantumGate extends QuantumGate {
    private QuantumGateOperation operation;

    public CustomQuantumGate(String name, QuantumGateOperation operation) {
        super(name);
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null.");
        }
        this.operation = operation;
    }

    @Override
    public void apply(double[] stateVector) {
        operation.apply(stateVector);
    }

    public interface QuantumGateOperation {
        void apply(double[] stateVector);
    }
}
