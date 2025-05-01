package com.dragonmerge.view;

import java.nio.file.Paths;

import com.dragonmerge.model.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import com.dragonmerge.utils.*;

/**
 * Represents the main view of the Dragon Merge game.
 * This class handles the initial display, level selection, and navigation to
 * other views such as the shop and inventory.
 */
public class MainView extends BorderPane {
    private Stage primaryStage;
    private Inventory inventory;
    private Label caliceCountLabel;
    private int currentLevel = 1;
    private ImageView dragonView;
    private Game game;
    private GameState gameState;
    private MediaPlayer mediaPlayer;
    private Button muteButton;
    private ImageView muteIconView;
    private Image muteIcon;
    private Image unmuteIcon;
    private AudioManager audioManager;

    /** Path to the resources directory. */
    private static final String RESOURCES_PATH = "file:src/ressources/";

    /**
     * Constructs a new MainView.
     *
     * @param primaryStage the primary stage of the application
     * @param game         the game instance
     * @param gameState    the current state of the game
     */
    public MainView(Stage primaryStage, Game game, GameState gameState) {
        this.primaryStage = primaryStage;
        this.game = game;
        this.gameState = gameState;
        this.audioManager = AudioManager.getInstance();
        this.currentLevel = game.getCurrentLevelIndex();
        initializeView();
    }

    /**
     * Initializes the main view including background, buttons, and layout setup.
     */
    private void initializeView() {
        audioManager.playMusic();
        Image levelImage = new Image(RESOURCES_PATH + "img/level_map.png");
        ImageView levelImageView = new ImageView(levelImage);
        levelImageView.setFitWidth(1350);
        levelImageView.setFitHeight(800);
        levelImageView.setPreserveRatio(false);
        levelImageView.setStyle("-fx-border-color: blue;");

        StackPane layout = new StackPane();
        layout.getChildren().add(levelImageView);

        addFlyingDragon(layout);
        addLevelButtons(layout);

        Image caliceIcon = new Image(RESOURCES_PATH + "img/caliceIcon1.png");
        ImageView caliceIconView = new ImageView(caliceIcon);
        caliceIconView.setFitWidth(60);
        caliceIconView.setFitHeight(65);

        caliceCountLabel = new Label(String.valueOf(gameState.getCalices()));
        caliceCountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: black; -fx-font-weight: bold;");
        StackPane caliceDisplay = new StackPane(caliceIconView, caliceCountLabel);
        StackPane.setAlignment(caliceIconView, Pos.CENTER);
        StackPane.setAlignment(caliceCountLabel, Pos.CENTER);

        Image shopIcon = new Image(RESOURCES_PATH + "img/shopIcon.png");
        ImageView shopIconView = new ImageView(shopIcon);
        shopIconView.setFitWidth(55);
        shopIconView.setFitHeight(55);

        Button shopButton = new Button();
        shopButton.setGraphic(shopIconView);
        shopButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
        shopButton.setOnAction(e -> openShop());

        Image inventoryIcon = new Image(RESOURCES_PATH + "img/bag.png");
        ImageView inventoryIconView = new ImageView(inventoryIcon);
        inventoryIconView.setFitWidth(55);
        inventoryIconView.setFitHeight(55);

        Button inventoryButton = new Button();
        inventoryButton.setGraphic(inventoryIconView);
        inventoryButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
        inventoryButton.setOnAction(e -> openInventory());

        this.unmuteIcon = new Image(RESOURCES_PATH + "img/sound.png");
        this.muteIcon = new Image(RESOURCES_PATH + "img/no-sound.png");
        this.muteIconView = new ImageView(unmuteIcon);
        muteIconView.setFitWidth(40);
        muteIconView.setFitHeight(40);

        this.muteButton = new Button();
        muteButton.setGraphic(muteIconView);
        muteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");

        muteButton.setOnAction(e -> toggleMute());

        HBox topLeftBox = new HBox(10, caliceDisplay, shopButton, inventoryButton, muteButton);
        topLeftBox.setAlignment(Pos.CENTER_LEFT);
        topLeftBox.setPadding(new Insets(10, 20, 10, 20));
        topLeftBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        layout.getChildren().add(topLeftBox);
        StackPane.setAlignment(topLeftBox, Pos.TOP_LEFT);

        this.setCenter(layout);
        updateViewState();
    }

    /**
     * Adds a flying dragon animation to the layout.
     * 
     * @param layout The stack pane to which the dragon animation is added.
     */
    private void addFlyingDragon(StackPane layout) {
        Image dragonGif = new Image(RESOURCES_PATH + "gif/gif_left.gif");
        dragonView = new ImageView(dragonGif);
        dragonView.setFitWidth(100);
        dragonView.setFitHeight(100);

        updateDragonPosition();

        dragonView.setMouseTransparent(true);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5), dragonView);
        transition.setAutoReverse(true);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();

        layout.getChildren().add(dragonView);
    }

    private void updateGameState() {
        GameState gameState = com.dragonmerge.Main.getGameState();
        gameState.setCurrentLevel(this.currentLevel);
        gameState.setCalices(gameState.getCalices());
    }

    private void updateDragonPosition() {
        int level = game.getCurrentLevelIndex() + 1;

        int[][] levelPositions = {
                // x 0 = gauche; y 0 = haut;
                { 15, 240 }, // 1
                { -350, 150 }, // 2
                { -165, 30 }, // 3
                { -350, -70 }, // 4
                { -200, -240 }, // 5
        };

        if (level - 1 < levelPositions.length) {
            dragonView.setTranslateX(levelPositions[level - 1][0]);
            dragonView.setTranslateY(levelPositions[level - 1][1]);
        }
    }

    /**
     * Adds level selection buttons to the layout.
     * 
     * @param layout The stack pane to which the level buttons are added.
     */
    private void addLevelButtons(StackPane layout) {
        int[][] levelPositions = {
                { 10, 265 }, // 1
                { -350, 170 }, // 2
                { -165, 50 }, // 3
                { -350, -70 }, // 4
                { -200, -220 }, // 5
        };

        for (int i = 0; i < levelPositions.length; i++) {
            int level = i + 1;
            Button levelButton = new Button();
            levelButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            levelButton.setPrefSize(150, 150);

            levelButton.setTranslateX(levelPositions[i][0]);
            levelButton.setTranslateY(levelPositions[i][1]);

            levelButton.setMouseTransparent(false);
            levelButton.setFocusTraversable(true);

            levelButton.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    selectLevel(level);
                }
            });

            layout.getChildren().add(levelButton);
        }
    }

    /**
     * Selects a level and updates the game state and dragon position.
     * 
     * @param level The level to select.
     */
    private void selectLevel(int level) {
        this.currentLevel = level;
        updateGameState();
        updateDragonPosition();
        startLevel(level);
    }

    /**
     * Starts the selected level.
     * 
     * @param level The level to start.
     */
    private void startLevel(int level) {
        if (level == 1 || gameState.getCompletedLevels().contains(level - 1)) {
            Player player = game.getPlayer();
            if (gameState.getDragons() <= 0) {
                showMessage("Vous n'avez pas de dragon. Veuillez fusionner des oeufs pour en obtenir un.");
            } else if (gameState.getCalices() >= 2) {
                if (audioManager != null) {
                    audioManager.pauseMusic();
                }
                gameState.setCalices(gameState.getCalices() - 2);
                game.setCurrentLevelIndex(level - 1);
                player.resetDragon();
                BattleView battleView = new BattleView(player, primaryStage, level, game, gameState);
                Scene combatScene = new Scene(battleView, 1350, 800);
                primaryStage.setScene(combatScene);
            } else {
                showMessage("Pas assez de calices pour démarrer le combat.");
            }
        } else {
            showMessage("Veuillez réussir le niveau précédent avant de jouer à celui-ci.");
        }
    }

    private void openShop() {
        audioManager.playMusic();
        ShopView shopView = new ShopView(primaryStage, game.getPlayer().getInventory(), game, gameState, audioManager);
        Scene shopScene = new Scene(shopView.getLayout(), 1350, 800);
        primaryStage.setScene(shopScene);
        updateCaliceDisplay();
    }

    private void openInventory() {
        audioManager.playMusic();
        InventoryView inventoryView = new InventoryView(new Inventory(), primaryStage, game, gameState, audioManager);
        Scene inventoryScene = new Scene(inventoryView.getPane(), 1350, 800);
        primaryStage.setScene(inventoryScene);
        updateCaliceDisplay();
    }

    private void updateCaliceDisplay() {
        caliceCountLabel.setText(String.valueOf(gameState.getCalices()));
    }

    private void toggleMute() {
        audioManager.toggleMute();
        updateMuteButton();
    }

    private void updateMuteButton() {
        if (audioManager.isMuted()) {
            muteIconView.setImage(muteIcon);
        } else {
            muteIconView.setImage(unmuteIcon);
        }
    }

    private void updateViewState() {
        updateMuteButton();
        audioManager.playMusic();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}