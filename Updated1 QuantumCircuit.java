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
        this.numberOfQubits = numberOfQubits;
        this.gates = new ArrayList<>();
        this.noiseModels = new ArrayList<>();
        this.measurement = new Measurement();
    }

    public void addGate(QuantumGate gate) {
        gates.add(gate);
    }

    public void addNoiseModel(NoiseModel noiseModel) {
        noiseModels.add(noiseModel);
    }

    public void execute(double[] stateVector) {
        for (QuantumGate gate : gates) {
            gate.apply(stateVector);
            applyNoise(stateVector);
        }
    }

    private void applyNoise(double[] stateVector) {
        for (NoiseModel noiseModel : noiseModels) {
            noiseModel.apply(stateVector);
        }
    }

    public int measure(double[] stateVector) {
        return measurement.measure(stateVector);
    }

    public int getNumberOfQubits() {
        return numberOfQubits;
    }
}
