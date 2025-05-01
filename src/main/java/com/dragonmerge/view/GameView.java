package com.dragonmerge.view;

import com.dragonmerge.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Represents the game view in the Dragon Merge game.
 * This class handles the end game screen and game restart functionality.
 */
public class GameView extends BorderPane {
    private static final String BACKGROUND_IMAGE_PATH = "file:src/ressources/img/flou_background.jpeg";
    private static final String WIN_IMAGE_PATH = "file:src/ressources/img/YouWin.png";
    private static final String LOSE_IMAGE_PATH = "file:src/ressources/img/YouLose.png";
    private static final int WINDOW_WIDTH = 1350;
    private static final int WINDOW_HEIGHT = 800;

    private final Stage primaryStage;
    private final BattleView battleView;
    private int currentLevel;
    private final Game game;
    private boolean playerWon;
    private final GameState gameState;

    /**
     * Constructs a new GameView.
     *
     * @param primaryStage the primary stage of the application
     * @param battleView   the battle view
     * @param currentLevel the current level
     * @param game         the game instance
     * @param gameState    the game state
     */
    public GameView(Stage primaryStage, BattleView battleView, int currentLevel, Game game, GameState gameState) {
        this.primaryStage = primaryStage;
        this.currentLevel = currentLevel;
        this.game = game;
        this.gameState = gameState;
        this.battleView = battleView;
    }

    /**
     * Displays the end screen based on whether the player won or lost.
     *
     * @param playerWon true if the player won, false otherwise
     */
    public void showEndScreen(boolean playerWon) {
        this.playerWon = playerWon;

        battleView.clearGameElements();
        setBackground(createBackground());

        StackPane centeredOverlay = createCenteredOverlay();
        this.setCenter(centeredOverlay);
    }

    private Background createBackground() {
        Image backgroundImg = new Image(BACKGROUND_IMAGE_PATH);
        BackgroundImage background = new BackgroundImage(
                backgroundImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        return new Background(background);
    }

    private StackPane createCenteredOverlay() {
        Rectangle overlayBackground = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);
        overlayBackground.setFill(Color.rgb(0, 0, 0, 0.5));

        ImageView endImageView = createEndImageView();
        VBox buttonBox = createButtonBox();

        VBox layout = new VBox(20, endImageView, buttonBox);
        layout.setAlignment(Pos.CENTER);

        StackPane centeredOverlay = new StackPane(overlayBackground, layout);
        centeredOverlay.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        centeredOverlay.setAlignment(Pos.CENTER);

        return centeredOverlay;
    }

    private ImageView createEndImageView() {
        String imagePath = playerWon ? WIN_IMAGE_PATH : LOSE_IMAGE_PATH;
        ImageView endImageView = new ImageView(new Image(imagePath));
        endImageView.setPreserveRatio(true);
        endImageView.setFitWidth(300);
        return endImageView;
    }

    private VBox createButtonBox() {
        Button actionButton = playerWon ? createNextLevelButton() : createRetryButton();
        Button homeButton = createHomeButton();

        VBox buttonBox = new VBox(15, actionButton, homeButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private Button createNextLevelButton() {
        Button nextLevelButton = new Button("Next Level");
        nextLevelButton.setOnAction(e -> {
            currentLevel++;
            game.setCurrentLevelIndex(currentLevel - 1);
            restartGame();
        });
        return nextLevelButton;
    }

    private Button createRetryButton() {
        Button retryButton = new Button("Replay");
        retryButton.setOnAction(e -> restartGame());
        return retryButton;
    }

    private Button createHomeButton() {
        Button homeButton = new Button("Back to home");
        homeButton.setOnAction(e -> goToMainView());
        return homeButton;
    }

    private void restartGame() {
        Player player = game.getPlayer();

        if (playerWon) {
            startNewBattle(player);
        } else {
            if (gameState.getCalices() >= 2) {
                gameState.setCalices(gameState.getCalices() - 2);
                startNewBattle(player);
            } else {
                showMessage("Pas assez de calices pour démarrer le combat.");
            }
        }
    }

    private void startNewBattle(Player player) {
        player.resetDragon();
        BattleView newBattleView = new BattleView(player, primaryStage, currentLevel, game, gameState);
        Scene newScene = new Scene(newBattleView, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(newScene);
    }

    private void goToMainView() {
        MainView mainView = new MainView(primaryStage, game, gameState);
        Scene newSceneMain = new Scene(mainView, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(newSceneMain);
    }

    private void showMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
