/*
 * Copyright (c) 2024, Devin B. Royal
 * All rights reserved.
 */

public class FindJavaVersion {
    public static void main(String args[]) {
        //finds Java version
        String str = System.getProperty("java.version");
        //prints Java version
        System.out.println("Java Version is: " + str);
    }
}
