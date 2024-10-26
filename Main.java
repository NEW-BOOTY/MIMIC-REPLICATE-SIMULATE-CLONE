/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class Main {
    public static void main(String[] args) {
        int numberOfQubits = 3; // Example with 3 qubits
        int markedElementIndex = 5; // Example index for the marked element
        double[] stateVector = new double[1 << numberOfQubits];

        // Initialize state vector
        for (int i = 0; i < stateVector.length; i++) {
            stateVector[i] = 1.0 / Math.sqrt(stateVector.length);
        }

        GroverOracle oracle = new ExampleOracle(markedElementIndex);
        GroverSearch groverSearch = new GroverSearch(numberOfQubits, oracle);

        groverSearch.initialize();

        int iterations = (int) (Math.PI / 4 * Math.sqrt(stateVector.length));

        for (int i = 0; i < iterations; i++) {
            groverSearch.addGroverIteration();
        }

        groverSearch.execute(stateVector);

        // Print the state vector
        for (int i = 0; i < stateVector.length; i++) {
            System.out.printf("State |%d>: %.5f\n", i, stateVector[i]);
        }
    }
}
--------------------------------------------------------------------
This code sets up and executes Grover's search algorithm using a simple oracle that marks a specified element. The algorithm will amplify the amplitude of the marked element's state, making it more likely to be measured.
--------------------------------------------------------------------
/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class Main {
    public static void main(String[] args) {
        int numberOfQubits = 3; // Example with 3 qubits
        int markedElementIndex = 5; // Example index for the marked element
        double[] stateVector = new double[1 << numberOfQubits];

        // Initialize state vector
        for (int i = 0; i < stateVector.length; i++) {
            stateVector[i] = 1.0 / Math.sqrt(stateVector.length);
        }

        GroverOracle oracle = new ExampleOracle(markedElementIndex);
        GroverSearch groverSearch = new GroverSearch(numberOfQubits, oracle);

        groverSearch.initialize();

        int iterations = (int) (Math.PI / 4 * Math.sqrt(stateVector.length));

        for (int i = 0; i < iterations; i++) {
            groverSearch.addGroverIteration();
        }

        groverSearch.execute(stateVector);

        // Print the state vector after Grover's search
        System.out.println("State vector after Grover's search:");
        for (int i = 0; i < stateVector.length; i++) {
            System.out.printf("State |%d>: %.5f\n", i, stateVector[i]);
        }

        // Demonstrate additional gates
        QuantumCircuit circuit = new QuantumCircuit(numberOfQubits);
        circuit.addGate(new PauliXGate());
        circuit.addGate(new PauliYGate());
        circuit.addGate(new PauliZGate());
        circuit.addGate(new ToffoliGate(0, 1, 2));

        // Reinitialize state vector
        for (int i = 0; i < stateVector.length; i++) {
            stateVector[i] = 1.0 / Math.sqrt(stateVector.length);
        }

        circuit.execute(stateVector);

        // Print the state vector after applying additional gates
        System.out.println("State vector after applying additional gates:");
        for (int i = 0; i < stateVector.length; i++) {
            System.out.printf("State |%d>: %.5f\n", i, stateVector[i]);
        }
    }
}
--------------------------------------------------------------------
This completes the implementation of the quantum gates and circuits along with the integration of Grover's search algorithm.
--------------------------------------------------------------------
