/*
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

import java.util.HashMap;
import java.util.Map;

public class SymbolTable<K, V> {

    // Internal storage for the symbol table
    private final Map<K, V> table;

    // Constructor to initialize the symbol table
    public SymbolTable() {
        this.table = new HashMap<>();
    }

    // Method to add a key-value pair to the symbol table
    public void put(K key, V value) {
        table.put(key, value);
    }

    // Method to get the value associated with a key
    public V get(K key) {
        return table.get(key);
    }

    // Method to check if the symbol table contains a specific key
    public boolean contains(K key) {
        return table.containsKey(key);
    }

    // Method to remove a key-value pair from the symbol table
    public V remove(K key) {
        return table.remove(key);
    }

    // Method to clear the symbol table
    public void clear() {
        table.clear();
    }

    // Method to get the current size of the symbol table
    public int size() {
        return table.size();
    }

    // Method to check if the symbol table is empty
    public boolean isEmpty() {
        return table.isEmpty();
    }

    public static void main(String[] args) {
        // Example usage of SymbolTable
        SymbolTable<String, Integer> symbolTable = new SymbolTable<>();

        // Adding elements to the symbol table
        symbolTable.put("a", 1);
        symbolTable.put("b", 2);
        symbolTable.put("c", 3);

        // Retrieving an element from the symbol table
        System.out.println("Value for key 'b': " + symbolTable.get("b"));

        // Checking if a key exists
        System.out.println("Contains key 'a': " + symbolTable.contains("a"));

        // Removing an element
        symbolTable.remove("a");
        System.out.println("Contains key 'a' after removal: " + symbolTable.contains("a"));

        // Checking the size
        System.out.println("Size of symbol table: " + symbolTable.size());

        // Clearing the symbol table
        symbolTable.clear();
        System.out.println("Is symbol table empty: " + symbolTable.isEmpty());
    }
}

// This SymbolTable class provides a generic implementation of a symbol table in Java.
// It supports operations such as insertion (put), retrieval (get), deletion (remove),
// and checking for the existence of keys (contains). The example in the main method
// demonstrates how to use this class in a real-world scenario.
