package com.example.app.services;

import com.example.app.exceptions.FuxkUMeanException;
import com.example.app.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 *
 * Unauthorized use, distribution, or reproduction of this code and/or software is prohibited without written consent from the author. If another entity, person, corporation, or organization profits from this creation, software, and/or code, then the profit must be split 50/50 with the author. Any further sharing must also adhere to these terms. For any questions, please contact the author. 
 * Email: PAY_ME@MY.COM; JAVA-DEVELOPER@PROGRAMMER.NET
 */

public class UserService {
    private List<User> users = new ArrayList<>();

    public void addUser(User user) throws FuxkUMeanException {
        if (user.getUsername() == null || user.getEmail() == null) {
            throw new FuxkUMeanException("User information is incomplete.");
        }
        users.add(user);
    }

    public User getUserByUsername(String username) throws FuxkUMeanException {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new FuxkUMeanException("User not found."));
    }
}
