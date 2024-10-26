<!--
 * Copyright © 2024 Devin B. Royal
 *
 * Permission is hereby granted, for a limited time, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 -->

import java.io.IOException;

public class SingletonScriptRunner {
    // The single instance of the class
    private static SingletonScriptRunner instance;

    // Private constructor prevents direct instantiation from other classes
    private SingletonScriptRunner() {
    }

    // Method to get the single instance of the class
    public static SingletonScriptRunner getInstance() {
        if (instance == null) {
            instance = new SingletonScriptRunner();
        }
        return instance;
    }

    // Method to run a script with the provided path
    public void runScript(String scriptPath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
            Process process = processBuilder.start();
            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Script executed with exit code: " + exitCode);
        } catch (IOException e) {
            System.out.println("An error occurred while trying to run the script: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("The script execution was interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the script path as an argument.");
            return;
        }

        // Get the Singleton instance and run the script
        SingletonScriptRunner runner = SingletonScriptRunner.getInstance();
        runner.runScript(args[0]);
    }
}


//  The SingletonScriptRunner class uses the Singleton pattern to ensure only one instance of the class can be created.
The runScript method is used to execute a script provided via the command line argument.
The main method checks for the script path argument and uses the Singleton instance to run the script.
To use this program, compile it and run it from the command line, passing the path to the script you want to execute as an argument:
java SingletonScriptRunner path/to/your/script.sh

Replace path/to/your/script.sh with the actual path to the script. This setup ensures that the script running functionality is encapsulated within a Singleton instance, providing a controlled environment for script execution. 