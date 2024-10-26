/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public abstract class QuantumGate {
    protected String name;

    public QuantumGate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void apply(double[] stateVector);
}
