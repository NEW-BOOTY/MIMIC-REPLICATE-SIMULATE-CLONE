/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */

package com.example;

public class Authenticator {
    public boolean authenticate(String username, String password) {
        // Add your authentication logic here
        return "admin".equals(username) && "password".equals(password);
    }
}

/** Copyright © 2024 Devin B. Royal. All Rights Reserved. */
