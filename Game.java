/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.imaginarium‭;

public class Game {
    private boolean isRunning;
    private AICompanion aiCompanion;
    private World world;

    public Game() {
        this.isRunning = false;
        this.aiCompanion = new AICompanion();
        this.world = new World();
    }

    public void start() {
        this.isRunning = true;
        init();
        run();
    }

    private void init() {
        System.out.println("Initializing game...");
        // Initialization code here
    }

    private void run() {
        while (isRunning) {
            // Main game loop
            update();
            render();
        }
    }

    private void update() {
        // Game update logic
    }

    private void render() {
        // Game rendering logic
    }

    public void stop() {
        this.isRunning = false;
    }
}
