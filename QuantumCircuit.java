/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuit {
    private List<QuantumGate> gates;
    private int numberOfQubits;

    public QuantumCircuit(int numberOfQubits) {
        this.numberOfQubits = numberOfQubits;
        this.gates = new ArrayList<>();
    }

    public void addGate(QuantumGate gate) {
        gates.add(gate);
    }

    public void execute(double[] stateVector) {
        for (QuantumGate gate : gates) {
            gate.apply(stateVector);
        }
    }

    public int getNumberOfQubits() {
        return numberOfQubits;
    }
}
