/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

/**
 *
 * @author saljack
 */
public enum GameMode {

    SURVIVAL(0), MILLIONAIRE(1), LEVEL(2), FREEMODE(3);

    public static GameMode values[] = {SURVIVAL, MILLIONAIRE, LEVEL, FREEMODE};
    private final int value;

    private GameMode(int value) {
        this.value = value;
    }

    public int getID() {
        return value;
    }
}
