/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class GroverSearch {

    private int numberOfQubits;
    private GroverOracle oracle;
    private GroverDiffusionOperator diffusionOperator;
    private QuantumCircuit circuit;

    public GroverSearch(int numberOfQubits, GroverOracle oracle) {
        this.numberOfQubits = numberOfQubits;
        this.oracle = oracle;
        this.diffusionOperator = new GroverDiffusionOperator();
        this.circuit = new QuantumCircuit(numberOfQubits);
    }

    public void initialize() {
        // Apply Hadamard gate to all qubits
        for (int i = 0; i < numberOfQubits; i++) {
            circuit.addGate(new HadamardGate());
        }
    }

    public void addGroverIteration() {
        circuit.addGate(oracle);
        circuit.addGate(diffusionOperator);
    }

    public void execute(double[] stateVector) {
        circuit.execute(stateVector);
    }

    public QuantumCircuit getCircuit() {
        return circuit;
    }
}
