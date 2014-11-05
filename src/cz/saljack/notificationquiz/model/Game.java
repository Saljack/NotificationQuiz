/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.saljack.notificationquiz.model;

import android.os.Bundle;

/**
 *
 * @author saljack
 */
public class Game implements Bundleable<Game> {

    public final static String TAG = Game.class.getName();

    private static final String GAME_MODE = "GAME_MODE";
    private static final String SCORE = "SCORE";

    private GameMode gameMode;
    private int score;

    public Game() {
        gameMode = GameMode.FREEMODE;
        score = 0;
    }

    public Game(GameMode gameMode) {
        this.gameMode = gameMode;
        score = 0;
    }

    public static Game fromBundle(Bundle bundle) {
        Game game = new Game();
        if (bundle != null) {
            game.gameMode = GameMode.values[bundle.getInt(GAME_MODE, GameMode.FREEMODE.getID())];
            game.score = bundle.getInt(SCORE, 0);
        }
        return game;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(GAME_MODE, gameMode.getID());
        bundle.putInt(SCORE, score);
        return bundle;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPoint() {
        ++score;
    }

    public void addPoints(int points) {
        score += points;
    }

}
