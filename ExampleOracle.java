/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.quantumcomputing;

public class ExampleOracle extends GroverOracle {

    private int markedElementIndex;

    public ExampleOracle(int markedElementIndex) {
        this.markedElementIndex = markedElementIndex;
    }

    @Override
    public void apply(double[] stateVector) {
        // Mark the specified element by flipping its amplitude
        stateVector[markedElementIndex] *= -1;
    }
}
