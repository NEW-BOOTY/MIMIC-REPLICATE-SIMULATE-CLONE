/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public abstract class GroverOracle extends QuantumGate {

    public GroverOracle() {
        super("GroverOracle");
    }

    @Override
    public abstract void apply(double[] stateVector);
}
