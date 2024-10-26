/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

package com.imaginarium‭;

public class Character {
    private String name;
    private String appearance;
    private String abilities;

    public Character(String name, String appearance, String abilities) {
        this.name = name;
        this.appearance = appearance;
        this.abilities = abilities;
    }

    public void customize(String newAppearance, String newAbilities) {
        this.appearance = newAppearance;
        this.abilities = newAbilities;
    }
}
