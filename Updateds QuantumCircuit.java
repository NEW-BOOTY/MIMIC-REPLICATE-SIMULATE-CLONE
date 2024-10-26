/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuit {
    private List<QuantumGate> gates;
    private List<NoiseModel> noiseModels;
    private int numberOfQubits;
    private Measurement measurement;

    public QuantumCircuit(int numberOfQubits) {
        if (numberOfQubits <= 0) {
            throw new IllegalArgumentException("Number of qubits must be positive.");
        }
        this.numberOfQubits = numberOfQubits;
        this.gates = new ArrayList<>();
        this.noiseModels = new ArrayList<>();
        this.measurement = new Measurement();
    }

    public void addGate(QuantumGate gate) {
        if (gate == null) {
            throw new IllegalArgumentException("Gate cannot be null.");
        }
        gates.add(gate);
    }

    public void addNoiseModel(NoiseModel noiseModel) {
        if (noiseModel == null) {
            throw new IllegalArgumentException("Noise model cannot be null.");
        }
        noiseModels.add(noiseModel);
    }

    public void execute(double[] stateVector) {
        if (stateVector == null || stateVector.length != (1 << numberOfQubits)) {
            throw new IllegalArgumentException("Invalid state vector.");
        }
        for (QuantumGate gate : gates) {
            try {
                gate.apply(stateVector);
                applyNoise(stateVector);
            } catch (Exception e) {
                System.err.println("Error applying gate " + gate.getName() + ": " + e.getMessage());
                throw e;
            }
        }
    }

    private void applyNoise(double[] stateVector) {
        for (NoiseModel noiseModel : noiseModels) {
            noiseModel.apply(stateVector);
        }
    }

    public int measure(double[] stateVector) {
        if (stateVector == null || stateVector.length != (1 << numberOfQubits)) {
            throw new IllegalArgumentException("Invalid state vector.");
        }
        return measurement.measure(stateVector);
    }

    public int getNumberOfQubits() {
        return numberOfQubits;
    }
}
