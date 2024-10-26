/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AI {
    // A map to store predefined responses for specific keywords
    private static Map<String, String> responses = new HashMap<>();

    // Static block to initialize the responses map
    static {
        responses.put("hello", "Hello! What should we play today?");
        responses.put("help", "Sure, I'm here to help. What do you need assistance with?");
        responses.put("search", "You can use our search engine to find information. Just type your query.");
        responses.put("create", "I can create 'ANYTHING'. What can you think of?");
        // Add more responses as needed
    }

    public static void main(String[] args) {
        // Scanner object to read user input from the console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the AI. Type 'exit' to quit.");

        // Infinite loop to keep the program running until the user types 'exit'
        while (true) {
            try {
                System.out.print("You: ");
                String input = scanner.nextLine().toLowerCase(); // Read user input and convert to lowercase
                if (input.equals("exit")) {
                    break; // Exit the loop if the user types 'exit'
                }
                String response = getResponse(input); // Get the response based on user input
                System.out.println("AI: " + response);
            } catch (Exception e) {
                // Handle any exceptions that occur during input reading and response generation
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close(); // Close the scanner to prevent resource leaks
    }

    // Method to get the response based on user input
    private static String getResponse(String input) {
        for (String key : responses.keySet()) {
            if (input.contains(key)) {
                return responses.get(key); // Return the response if the input contains a known keyword
            }
        }
        return "I'm sorry, I don't understand that. Can you please rephrase?"; // Default response for unknown input
    }
}
