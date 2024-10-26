/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

import java.util.Random;

public class DepolarizingNoiseModel extends NoiseModel {
    private double probability;
    private Random random;

    public DepolarizingNoiseModel(double probability) {
        super("Depolarizing Noise");
        this.probability = probability;
        this.random = new Random();
    }

    @Override
    public void apply(double[] stateVector) {
        int length = stateVector.length;
        for (int i = 0; i < length; i++) {
            if (random.nextDouble() < probability) {
                stateVector[i] = 0.0;
            }
        }
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuit {
    private List<QuantumGate> gates;
    private List<NoiseModel> noiseModels;
    private int numberOfQubits;

    public QuantumCircuit(int numberOfQubits) {
        this.numberOfQubits = numberOfQubits;
        this.gates = new ArrayList<>();
        this.noiseModels = new ArrayList<>();
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

    public int getNumberOfQubits() {
        return numberOfQubits;
    }
}

