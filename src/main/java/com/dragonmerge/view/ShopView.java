package com.dragonmerge.view;

import com.dragonmerge.utils.*;

import com.dragonmerge.model.Inventory;
import com.dragonmerge.model.GameState;
import com.dragonmerge.model.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

/**
 * Represents the shop view of the game where players can purchase items.
 */
public class ShopView {
    private Stage primaryStage;
    private StackPane layout;
    private Game game;
    private GameState gameState;
    private AudioManager audioManager;

    /**
     * Constructs the shop view with the given parameters.
     *
     * @param primaryStage The primary stage of the application.
     * @param inventory    The player's inventory.
     * @param game         The main game object.
     * @param gameState    The current state of the game.
     */
    public ShopView(Stage primaryStage, Inventory inventory, Game game, GameState gameState,
            AudioManager audioManager) {
        this.primaryStage = primaryStage;
        this.gameState = gameState;
        this.game = game;
        this.audioManager = audioManager;
        setupLayout();
        audioManager.playMusic();
    }

    /**
     * Sets up the layout of the shop view, including background, content, and
     * animations.
     */
    private void setupLayout() {
        layout = new StackPane();
        setBackground();

        VBox content = createContentVBox();
        VBox outDragons = createOutDragonsVBox();
        VBox movingDragons = createMovingDragonsVBox();
        HBox resourceBar = createResourceBar();

        layout.getChildren().addAll(resourceBar, movingDragons, createShopBackground(), outDragons, content);
        StackPane.setAlignment(resourceBar, Pos.TOP_RIGHT);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    private void setBackground() {
        Image shopBackgroundImage = new Image("file:src/ressources/img/background.jpeg");
        BackgroundImage shopBackground = new BackgroundImage(
                shopBackgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        layout.setBackground(new Background(shopBackground));
    }

    private ImageView createShopBackground() {
        Image fondShop = new Image("file:src/ressources/img/parchemin.png");
        ImageView fondShopView = new ImageView(fondShop);
        fondShopView.setFitWidth(1400);
        fondShopView.setFitHeight(1400);
        fondShopView.setPreserveRatio(true);
        return fondShopView;
    }

    /**
     * Creates a VBox containing the main content of the shop.
     * This includes shop items and a back button.
     *
     * @return A VBox containing the shop content.
     */
    private VBox createContentVBox() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Label shopLabel = createShopLabel();
        content.getChildren().add(shopLabel);

        content.getChildren().addAll(
                createItemSection("Oeuf de dragon", "egg.png", "Acheter pour 130 pièces", this::buyEggShop),
                createItemSection("nid de dragon", "nest.png", "Acheter pour 360 pièces", this::buyNestShop),
                createItemSection("Calice", "caliceIcon1.png", "Acheter pour 70 pièces", this::buyCaliceShop));

        Button backButton = createBackButton();
        content.getChildren().add(backButton);

        return content;
    }

    private Label createShopLabel() {
        Label shopLabel = new Label("BIENVENUE DANS LA BOUTIQUE");
        shopLabel.setStyle("-fx-text-fill: black");
        shopLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 35));
        return shopLabel;
    }

    /**
     * Creates an item section for the shop, including label, image, and buy button.
     *
     * @param labelText  The label text for the item.
     * @param imagePath  The image path for the item icon.
     * @param buttonText The text displayed on the purchase button.
     * @param buyAction  The action to be executed when the button is clicked.
     * @return A VBox representing the item section.
     */
    private VBox createItemSection(String labelText, String imagePath, String buttonText, Runnable buyAction) {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);

        Label itemLabel = new Label(labelText);
        itemLabel.setStyle("-fx-font-size: 20px; -fx-padding: 5px;");

        ImageView itemImageView = createItemImageView("file:src/ressources/img/" + imagePath);
        Button buyButton = createBuyButton(buttonText, buyAction);

        section.getChildren().addAll(itemLabel, itemImageView, buyButton);
        return section;
    }

    private ImageView createItemImageView(String imagePath) {
        Image itemImage = new Image(imagePath);
        ImageView itemImageView = new ImageView(itemImage);
        itemImageView.setFitWidth(60);
        itemImageView.setFitHeight(60);
        itemImageView.setPreserveRatio(false);
        itemImageView.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4e4e50, #2a2a2a);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 5);");
        return itemImageView;
    }

    private Button createBuyButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4e4e50, #2a2a2a);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 5);");
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #6e6e70, #4e4e50);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #FFA500;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,165,0,0.75), 10, 0.7, 0, 5);"));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4e4e50, #2a2a2a);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 5);"));
        button.setOnAction(e -> action.run());
        return button;
    }

    private Button createBackButton() {
        Button backButton = new Button("RETOUR AUX NIVEAUX DE JEU");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        backButton.setStyle(
                "-fx-padding: 10px;" +
                        "-fx-background-color: linear-gradient(#f1e22a, #ff4500);" +
                        "-fx-border-color: rgba(0,0,0,0.5);" +
                        "-fx-border-width: 2px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-padding: 10px;" +
                        "-fx-background-color: linear-gradient(#f1e22a, #ff4500);" +
                        "-fx-border-color: #f1e22a;" +
                        "-fx-border-width: 2px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-padding: 10px;" +
                        "-fx-background-color: linear-gradient(#f1e22a, #ff4500);" +
                        "-fx-border-color: rgba(0,0,0,0.5);" +
                        "-fx-border-width: 2px;"));
        backButton.setOnAction(e -> {
            audioManager.playMusic();
            showMainMenu();
        });
        return backButton;
    }

    private VBox createOutDragonsVBox() {
        VBox outDragons = new VBox(20);
        outDragons.setAlignment(Pos.CENTER);

        ImageView dragonRed1View = createDragonImageView("file:src/ressources/gif/red_down.gif", 80, 80, 250, 0);
        ImageView dragonIcon2View = createDragonImageView("file:src/ressources/gif/gif_down.gif", 80, 80, -250, -100);

        outDragons.getChildren().addAll(dragonRed1View, dragonIcon2View);
        return outDragons;
    }

    private ImageView createDragonImageView(String imagePath, double width, double height, double translateX,
            double translateY) {
        Image dragonImage = new Image(imagePath);
        ImageView dragonView = new ImageView(dragonImage);
        dragonView.setFitWidth(width);
        dragonView.setFitHeight(height);
        dragonView.setPreserveRatio(false);
        dragonView.setTranslateX(translateX);
        dragonView.setTranslateY(translateY);
        return dragonView;
    }

    /**
     * Creates a moving dragon animation in the shop.
     *
     * @param imagePath The image path of the dragon animation.
     * @param width     The width of the dragon image.
     * @param height    The height of the dragon image.
     * @param startX    The starting X coordinate of the animation.
     * @param endX      The ending X coordinate of the animation.
     * @param duration  The duration of the animation.
     * @param cycles    The number of animation cycles.
     * @return An ImageView representing the animated dragon.
     */
    private VBox createMovingDragonsVBox() {
        VBox movingDragons = new VBox(20);
        movingDragons.setAlignment(Pos.CENTER);

        ImageView redDragon3View = createMovingDragon("file:src/ressources/gif/red_left.gif", 38, 38, 945, -400, 30,
                10);
        ImageView redDragon4View = createMovingDragon("file:src/ressources/gif/red_left.gif", 35, 35, 945, -200, 24, 6);
        ImageView bossView = createMovingBoss();
        ImageView dragon2View = createMovingDragon("file:src/ressources/gif/gif_right.gif", 38, 38, -945, 380, 30, 10);
        ImageView dragon3View = createMovingDragon("file:src/ressources/gif/gif_right.gif", 41, 41, -935, 380, 30, 10);
        ImageView dragon4View = createMovingDragon("file:src/ressources/gif/gif_right.gif", 31, 31, -930, 380, 35, 10);

        movingDragons.getChildren().addAll(redDragon3View, redDragon4View, dragon2View, dragon3View, bossView,
                dragon4View);
        return movingDragons;
    }

    private ImageView createMovingDragon(String imagePath, double width, double height, double startX, double endX,
            double duration, int cycles) {
        ImageView dragonView = createDragonImageView(imagePath, width, height, startX, 0);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), dragonView);
        transition.setToX(endX);
        transition.setCycleCount(cycles);
        transition.setAutoReverse(false);
        transition.play();
        return dragonView;
    }

    private ImageView createMovingBoss() {
        Image bossImage = new Image("file:src/ressources/img/roadmap.png");
        ImageView bossView = new ImageView(bossImage);
        bossView.setFitWidth(210);
        bossView.setFitHeight(200);
        bossView.setPreserveRatio(false);

        TranslateTransition transitionBoss = new TranslateTransition(Duration.seconds(25), bossView);
        transitionBoss.setToX(-510);
        transitionBoss.setCycleCount(4);
        transitionBoss.setAutoReverse(true);

        PauseTransition delayBoss = new PauseTransition(Duration.seconds(55));
        delayBoss.setOnFinished(e -> transitionBoss.play());
        delayBoss.play();

        return bossView;
    }

    private HBox createResourceBar() {
        HBox resourceBar = new HBox(20);
        resourceBar.setId("resourceBar");
        resourceBar.setAlignment(Pos.TOP_RIGHT);

        Label coinsLabel = createResourceLabel("pieceIcon.png", gameState.getCoins());
        Label eggsLabel = createResourceLabel("egg.png", gameState.getEggs());
        Label calicesLabel = createResourceLabel("caliceIcon1.png", gameState.getCalices() + "/8");

        resourceBar.getChildren().addAll(coinsLabel, eggsLabel, calicesLabel);
        resourceBar.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        resourceBar.setTranslateX(-13);
        resourceBar.setTranslateY(13);

        return resourceBar;
    }

    private void refreshResourceBar() {
        HBox resourceBar = (HBox) layout.lookup("#resourceBar");
        resourceBar.getChildren().clear();

        Label coinsLabel = createResourceLabel("pieceIcon.png", gameState.getCoins());
        Label eggsLabel = createResourceLabel("egg.png", gameState.getEggs());
        Label calicesLabel = createResourceLabel("caliceIcon1.png", gameState.getCalices() + "/8");

        resourceBar.getChildren().addAll(coinsLabel, eggsLabel, calicesLabel);
    }

    private Label createResourceLabel(String iconPath, Object value) {
        Image icon = new Image("file:src/ressources/img/" + iconPath);
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(35);
        iconView.setFitHeight(35);
        iconView.setPreserveRatio(false);

        Label label = new Label("x" + value, iconView);
        label.setContentDisplay(ContentDisplay.LEFT);

        return label;
    }

    private void showMainMenu() {
        if (game != null) {
            MainView mainView = new MainView(primaryStage, game, gameState);
            Scene mainScene = new Scene(mainView, 1350, 800);
            primaryStage.setScene(mainScene);
        } else {
            System.out.println("Game is null");
        }
    }

    private void buyEggShop() {
        Alert confirmationEgg = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationEgg.setTitle("Confirmation d'achat");
        confirmationEgg.setHeaderText(null);
        confirmationEgg.setContentText("Voulez-vous acheter un oeuf ?");
        confirmationEgg.initModality(Modality.APPLICATION_MODAL);
        confirmationEgg.initOwner(primaryStage);
        confirmationEgg.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (gameState.buyEgg()) {
                    showAlert("Oeuf acheté", "Vous avez acheté un oeuf");
                    refreshResourceBar();
                } else {
                    showAlert("Pas assez de pieces", "Vous n'avez pas assez de pièces pour acheter un oeuf");
                }
            } else {
                showAlert("Achat annulé", "l'achat de l'oeuf a été annulé.");
            }
        });
    }

    private void buyNestShop() {
        Alert confirmationNest = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationNest.setTitle("Confirmation d'achat");
        confirmationNest.setHeaderText(null);
        confirmationNest.setContentText("Voulez-vous acheter un nid ?");
        confirmationNest.initModality(Modality.APPLICATION_MODAL);
        confirmationNest.initOwner(primaryStage);
        confirmationNest.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (gameState.buyDragonNest()) {
                    showAlert("nid acheté", "Vous avez acheté un nid");
                    refreshResourceBar();

                } else {
                    showAlert("pas assez de pieces", "Vous n'avez pas assez de pièces pour acheter un nid");
                }
            } else {
                showAlert("Achat annulé", "l'achat de nid a été annulé.");
            }
        });

    }

    private void buyCaliceShop() {
        Alert confirmationCalice = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationCalice.setTitle("Confirmation d'achat");
        confirmationCalice.setHeaderText(null);
        confirmationCalice.setContentText("Voulez-vous acheter un calice ?");
        confirmationCalice.initModality(Modality.APPLICATION_MODAL);
        confirmationCalice.initOwner(primaryStage);
        confirmationCalice.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (gameState.buyCalice()) {
                    showAlert("achat réussi",
                            "Vous avez acheté un calice. \nvous possédez " + gameState.getCalices() + "/8 calices.");
                    refreshResourceBar();
                } else if (gameState.getCalices() >= 8) {
                    showAlert("Calice max atteints", "Vous possédez le nombre maximum de calices.");
                } else {
                    showAlert("Pas assez de pièces", "Vous n'avez pas assez de pièces pour achteter un calice");
                }
            } else {
                showAlert("Achat annulé", "l'achat de calice a été annulé.");
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    /**
     * Returns the root layout of the shop view.
     *
     * @return The root StackPane containing the shop layout.
     */
    public StackPane getLayout() {
        return layout;
    }

}
