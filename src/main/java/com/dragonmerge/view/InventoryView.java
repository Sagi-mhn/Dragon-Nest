package com.dragonmerge.view;

import com.dragonmerge.model.Game;
import com.dragonmerge.model.GameState;
import com.dragonmerge.model.Inventory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.dragonmerge.utils.*;

/**
 * Represents the inventory view in the game, allowing players to view and
 * manage their resources.
 */
public class InventoryView {
    private final Pane pane;
    private final Stage primaryStage;
    private final Inventory inventory;
    private final VBox inventoryContainer;
    private final Game game;
    private final GameState gameState;
    private AudioManager audioManager;

    /** Path to the resources directory. */
    private static final String RESOURCES_PATH = "file:src/ressources/";

    /**
     * Constructs an InventoryView instance.
     *
     * @param inventory    The player's inventory.
     * @param primaryStage The primary stage of the application.
     * @param game         The main game object.
     * @param gameState    The current game state.
     */
    public InventoryView(Inventory inventory, Stage primaryStage, Game game, GameState gameState,
            AudioManager audioManager) {
        this.primaryStage = primaryStage;
        this.inventory = inventory;
        this.game = game;
        this.gameState = gameState;
        this.pane = new StackPane();
        this.audioManager = audioManager;
        this.inventoryContainer = new VBox();
        audioManager.playMusic();
        initializeView();
    }

    /**
     * Initializes the inventory view by setting up the background, items, and
     * buttons.
     */
    private void initializeView() {
        setupBackground();
        setupParchment();
        setupInventoryContainer();
        setupInventoryItems();
        setupBackButton();
        setupAddDragon();
    }

    private void setupBackground() {
        Image backgroundImage = new Image(RESOURCES_PATH + "img/flou_background.jpeg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        pane.setBackground(new Background(background));
    }

    private void setupParchment() {
        ImageView parchmentImage = new ImageView(new Image(RESOURCES_PATH + "img/parch.png"));
        parchmentImage.setFitWidth(800);
        parchmentImage.setPreserveRatio(true);
        StackPane.setAlignment(parchmentImage, Pos.CENTER);
        pane.getChildren().add(parchmentImage);
    }

    private void setupInventoryContainer() {
        inventoryContainer.setAlignment(Pos.CENTER);
        inventoryContainer.setSpacing(20);
        pane.getChildren().add(inventoryContainer);
    }

    private void setupInventoryItems() {
        inventoryContainer.getChildren().clear();
        HBox cardsContainer = new HBox(
                createInventoryCard(new Image(RESOURCES_PATH + "gif/gif_down.gif"), "Dragon",
                        "x " + gameState.getDragons()),
                createInventoryCard(new Image(RESOURCES_PATH + "img/egg.png"), "Eggs", "x " + gameState.getEggs()),
                createInventoryCard(new Image(RESOURCES_PATH + "img/pieceIcon.png"), "Coins",
                        "x " + gameState.getCoins()));
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setSpacing(30);
        inventoryContainer.getChildren().add(cardsContainer);
    }

    /**
     * Creates a styled inventory card for display in the inventory view.
     *
     * @param icon  The image to display on the card.
     * @param title The title of the inventory item.
     * @param value The value or quantity of the inventory item.
     * @return A VBox representing the inventory card.
     */
    private VBox createInventoryCard(Image icon, String title, String value) {
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(80);
        iconView.setFitHeight(80);
        iconView.setPreserveRatio(true);

        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", 24));
        titleText.setFill(Color.WHITE);

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Arial", 20));
        valueText.setFill(Color.GOLD);

        VBox textContainer = new VBox(titleText, valueText);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.setSpacing(10);

        VBox card = new VBox(iconView, textContainer);
        card.setAlignment(Pos.CENTER);
        card.setSpacing(15);
        card.setPadding(new javafx.geometry.Insets(20));

        setCardStyle(card);
        return card;
    }

    /**
     * Sets up the style and hover effects for an inventory card.
     *
     * @param card The VBox representing the inventory card.
     */
    private void setCardStyle(VBox card) {
        String baseStyle = "-fx-background-color: linear-gradient(to bottom, #2a2a2a, #1f1f1f);" +
                "-fx-border-color: #FFD700;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 5);";

        String hoverStyle = "-fx-background-color: linear-gradient(to bottom, #3a3a3a, #2a2a2a);" +
                "-fx-border-color: #FFA500;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,165,0,0.75), 10, 0.7, 0, 5);";

        card.setStyle(baseStyle);
        card.setOnMouseEntered(event -> card.setStyle(hoverStyle));
        card.setOnMouseExited(event -> card.setStyle(baseStyle));
    }

    private void setupBackButton() {
        Button backButton = createStyledButton("Retour");
        StackPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(backButton, new javafx.geometry.Insets(0, 0, 20, 20));
        backButton.setOnAction(event -> {
            audioManager.playMusic();
            goToMainView();
        });
        pane.getChildren().add(backButton);
    }

    private void setupAddDragon() {
        Button dragonButton = createStyledButton("Fusionner vos oeufs");
        StackPane.setAlignment(dragonButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(dragonButton, new javafx.geometry.Insets(0, 20, 20, 0));
        dragonButton.setOnAction(event -> addDragon());
        pane.getChildren().add(dragonButton);
    }

    /**
     * Creates a styled button with specific visual effects.
     *
     * @param text The text displayed on the button.
     * @return A styled Button instance.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 24));
        setButtonStyle(button);
        return button;
    }

    private void setButtonStyle(Button button) {
        String baseStyle = "-fx-background-color: linear-gradient(to bottom, #4e4e50, #2a2a2a);" +
                "-fx-text-fill: white;" +
                "-fx-border-color: #FFD700;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 5);";

        String hoverStyle = "-fx-background-color: linear-gradient(to bottom, #6e6e70, #4e4e50);" +
                "-fx-text-fill: white;" +
                "-fx-border-color: #FFA500;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,165,0,0.75), 10, 0.7, 0, 5);";

        button.setStyle(baseStyle);
        button.setOnMouseEntered(event -> button.setStyle(hoverStyle));
        button.setOnMouseExited(event -> button.setStyle(baseStyle));
    }

    private void goToMainView() {
        if (game != null) {
            MainView mainView = new MainView(primaryStage, game, gameState);
            Scene newSceneMain = new Scene(mainView, 1350, 800);
            primaryStage.setScene(newSceneMain);
        } else {
            System.out.println("Game object is null, cannot navigate to MainView");
        }
    }

    private void addDragon() {
        if (gameState.mergeEggs()) {
            showAlert("merge dragons", "Vous avez fusionné vos oeufs. \nDragon(s) possédé(s) : " +
                    (gameState.getDragons() + 1) + "\nOeufs restants : " + gameState.getEggs());
            gameState.setDragons(gameState.getDragons() + 1);
            System.out.println("Dragon: " + gameState.getDragons());
            System.out.println("Coins: " + gameState.getCoins());
            setupInventoryItems();
        } else {
            showAlert("can't merge dragons", "Vous n'avez pas assez d'oeufs pour les fusionner. \nOeufs possédés : " +
                    gameState.getEggs());
            System.out.println("Pas assez de pièces pour ajouter un dragon.");
        }
    }

    /**
     * Returns the root pane of the inventory view.
     *
     * @return The root Pane containing all elements of the inventory view.
     */
    public Pane getPane() {
        return pane;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }
}