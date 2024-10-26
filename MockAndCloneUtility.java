/*
 * Copyright (c) 2024 Devin Benard Royal
 * All rights reserved.
 */

import java.lang.reflect.Field;
import org.mockito.Mockito;

public class MockAndCloneUtility {

    // Method to clone an object
    public static <T> T clone(T object) {
        try {
            Class<?> clazz = object.getClass();
            T clone = (T) clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(object));
            }
            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }

    // Method to mock an object
    public static <T> T mock(Class<T> clazz) {
        return Mockito.mock(clazz);
    }

    // Main method for demonstration
    public static void main(String[] args) {
        // Example object to clone
        MyObject original = new MyObject("Hello", 123);
        MyObject cloned = clone(original);
        System.out.println("Original: " + original);
        System.out.println("Cloned: " + cloned);

        // Example of mocking
        MyObject mock = mock(MyObject.class);
        System.out.println("Mock: " + mock);
    }
}

// Example class to demonstrate cloning and mocking
class MyObject {
    private String text;
    private int number;

    public MyObject(String text, int number) {
        this.text = text;
        this.number = number;
    }

    @Override
    public String toString() {
        return "MyObject{text='" + text + "', number=" + number + "}";
    }
}
