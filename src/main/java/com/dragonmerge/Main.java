package com.dragonmerge;

import com.dragonmerge.model.Calice;
import com.dragonmerge.model.GameState;
import com.dragonmerge.model.Inventory;
import com.dragonmerge.utils.GameSaveManager;
import com.dragonmerge.view.MainView;
import com.dragonmerge.model.Game;
import com.dragonmerge.utils.GameSaveManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static GameState gameState;
    private static Calice calice;

    public void start(Stage primaryStage) {
        gameState = GameSaveManager.loadGame();
        if (gameState == null) {
            gameState = new GameState();
            gameState.setCoins(400);
            gameState.setCalices(8);
            gameState.setCurrentLevel(1);
        }

        Game game = new Game(gameState);
        MainView mainView = new MainView(primaryStage, game, gameState);

        Scene scene = new Scene(mainView, 1350, 800);
        primaryStage.setTitle("DragonNest");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GameSaveManager.saveGame(gameState);
        }));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static Calice getCalice() {
        return calice;
    }
}
