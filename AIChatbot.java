/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AIChatbot {
    private static Map<String, String> responses = new HashMap<>();

    static {
        responses.put("hello", "Hello! How can I assist you today?");
        responses.put("help", "Sure, I'm here to help. What do you need assistance with?");
        responses.put("search", "You can use our search engine to find information. Just type your query.");
        responses.put("weather", "You can check the weather by typing your location.");
        responses.put("time", "You can ask me for the current time.");
        responses.put("date", "You can ask me for today's date.");
        responses.put("news", "You can ask me for the latest news updates.");
        responses.put("joke", "Would you like to hear a joke?");
        responses.put("quote", "Would you like to hear an inspirational quote?");
        responses.put("music", "You can ask me to play some music.");
        responses.put("sports", "You can ask me for the latest sports updates.");
        responses.put("movie", "You can ask me for movie recommendations.");
        responses.put("food", "You can ask me for food recipes.");
        responses.put("travel", "You can ask me for travel recommendations.");
        responses.put("book", "You can ask me for book recommendations.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the AI Chatbot. Type 'exit' to quit.");

        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("exit")) {
                break;
            }
            String response = getResponse(input);
            System.out.println("Bot: " + response);
        }
        scanner.close(); // Close the scanner to prevent resource leak
    }

    private static String getResponse(String input) {
        for (String key : responses.keySet()) {
            if (input.contains(key)) {
                return responses.get(key);
            }
        }
        return "I'm sorry, I don't understand that. Can you please rephrase?";
    }
}
