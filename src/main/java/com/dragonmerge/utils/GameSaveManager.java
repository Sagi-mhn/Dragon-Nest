package com.dragonmerge.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.dragonmerge.model.GameState;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class GameSaveManager {

    private static final String SAVE_FILE = "game_state.json";

    public static void saveGame(GameState gameState) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            gson.toJson(gameState, writer);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public static GameState loadGame() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(SAVE_FILE)) {
            return gson.fromJson(reader, GameState.class);
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }
}
