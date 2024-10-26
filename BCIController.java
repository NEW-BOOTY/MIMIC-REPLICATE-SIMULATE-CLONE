// Copyright © 2024 Devin B. Royal. All Rights Reserved.
/*
MIT License

Copyright (c) 2024 Devin B. Royal. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy and requesting permission
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/



import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BCIController {
    private Game game;
    private BCI bci;
    private ScheduledExecutorService executor;

    public BCIController(Game game, BCI bci) {
        this.game = game;
        this.bci = bci;
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        // Schedule the BCI to update every 100 milliseconds
        executor.scheduleAtFixedRate(() -> {
            try {
                // Read the latest command from the BCI
                Command command = bci.readCommand();

                // Update the game state based on the command
                game.update(command);
            } catch (Exception e) {
                System.err.println("Error updating game: " + e.getMessage());
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            } 
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}

/*
In this example, BCIController is a class that uses a ScheduledExecutorService to continuously read commands from a BCI object and apply them to a Game object. The start() method begins this loop, and the stop() method ends it. Error handling is included to ensure that any exceptions thrown during the update process are caught and logged.
*/

