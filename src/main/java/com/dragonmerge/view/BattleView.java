package com.dragonmerge.view;

import com.dragonmerge.model.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;

import java.nio.file.Paths;
import java.util.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * The class represents the battlefield where player and opponents interact.
 * It manages all the visuals and interactions of a game level.
 */
public class BattleView extends BorderPane {
    /** Represents the player in the battle. */
    private Player player;

    /** List of all current opponents in the battle. */
    private List<Opponent> opponents;

    /** Visual representations of the opponents. */
    private List<OpponentView> opponentViews;

    /** The main pane where all game elements are drawn. */
    private Pane pane;

    /** The root stack pane that holds all other UI elements. */
    private StackPane root;

    /** A vertical box that displays the pause menu. */
    private VBox pauseOverlay;

    /** Holds the set of currently pressed keys. */
    private Set<KeyCode> keysPressed = new HashSet<>();

    /** Tracks the last time the player shot a projectile. */
    private long lastShootTime = 0;

    /** Minimum interval between shots. */
    private static final double SHOOT_INTERVAL_SECONDS = 0.5;

    /** Manages the projectiles in the view. */
    private ProjectileView projectileView;

    /** The primary stage for the application. */
    private Stage primaryStage;

    /** The current level number. */
    private int levelNumber;

    /** The game instance containing game logic. */
    private Game game;

    /** The battle logic handler. */
    private Battle battle;

    /** Represents the state of the game. */
    private GameState gameState;

    /** Displays the player's health. */
    private HealthBar playerHealthBar;

    /** Maps each opponent to their respective health bar. */
    private Map<Opponent, HealthBar> opponentHealthBars;

    /** Indicates if the game is currently paused. */
    private boolean isPaused = false;

    /** Indicates if the game has ended. */
    private boolean isGameOver = false;

    /** Element for the music */
    private MediaPlayer mediaPlayer;

    /** Path to the resources directory. */
    private static final String RESOURCES_PATH = "file:src/ressources/";

    /**
     * Indicates whether the ZQSD control mode is active.
     * If true, the player uses the keys Z, Q, S, D to move.
     * If false, the player uses the arrow keys for movement.
     */
    private boolean isZQSDControl = false;

    /**
     * Label displaying the currently active control mode (ZQSD or Arrow Keys).
     * Dynamically updated when the player switches the control mode.
     */
    private Label controlLabel;

    /**
     * Constructs a new BattleView.
     *
     * @param player       the player of the game
     * @param primaryStage the primary stage of the application
     * @param levelNumber  the current level number
     * @param game         the game instance
     * @param gameState    the current state of the game
     */
    public BattleView(Player player, Stage primaryStage, int levelNumber, Game game, GameState gameState) {
        System.out.println("Initialisation de BattleView pour le niveau : " + levelNumber);
        initializeFields(player, primaryStage, levelNumber, game, gameState);
        setupUI();
        setupGameLoop();
    }

    /**
     * Initializes fields used in the battle view.
     */
    private void initializeFields(Player player, Stage primaryStage, int levelNumber, Game game, GameState gameState) {
        playSound();
        this.player = player;
        this.primaryStage = primaryStage;
        this.levelNumber = levelNumber;
        this.game = game;
        this.gameState = gameState;
        this.battle = new Battle(player, levelNumber);
        this.opponents = battle.getOpponents();
        this.opponentViews = new ArrayList<>();
        this.opponentHealthBars = new HashMap<>();
        this.pane = new Pane();
        this.root = new StackPane();
        this.pauseOverlay = new VBox();
        this.projectileView = new ProjectileView(pane);
    }

    private void setupUI() {
        setupBackground();
        setupPauseOverlay();
        setupArena();
        setupPlayerHealthBar();
        setupIcon();
        setupOpponents();
        this.setCenter(root);
        setupKeyHandlers();
    }

    private void setupBackground() {
        Image backgroundImg = new Image(RESOURCES_PATH + "img/flou_background.jpeg");
        BackgroundImage background = new BackgroundImage(
                backgroundImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        pane.setBackground(new Background(background));
    }

    private void setupIcon() {
        double baseX = 20;
        double baseY = 60;
        double verticalSpacing = 50;

        ImageView pauseIcon = new ImageView(new Image(RESOURCES_PATH + "icons/lettre-p.png"));
        pauseIcon.setFitWidth(35);
        pauseIcon.setFitHeight(35);
        pauseIcon.setLayoutX(baseX);
        pauseIcon.setLayoutY(baseY);

        Label pauseLabel = new Label("Pause");
        pauseLabel.setFont(new Font("Arial", 12));
        pauseLabel.setTextFill(Color.DARKBLUE);
        pauseLabel.setLayoutX(baseX + 40);
        pauseLabel.setLayoutY(baseY + (pauseIcon.getFitHeight() / 2) - (pauseLabel.getFont().getSize() / 2));

        ImageView controlIcon = new ImageView(new Image(RESOURCES_PATH + "icons/lettre-c.png"));
        controlIcon.setFitWidth(35);
        controlIcon.setFitHeight(35);
        controlIcon.setLayoutX(baseX);
        controlIcon.setLayoutY(baseY + verticalSpacing);

        controlLabel = new Label("Flèches");
        controlLabel.setFont(new Font("Arial", 12));
        controlLabel.setTextFill(Color.DARKBLUE);
        controlLabel.setLayoutX(baseX + 40);
        controlLabel.setLayoutY(
                controlIcon.getLayoutY() + (controlIcon.getFitHeight() / 2) - (controlLabel.getFont().getSize() / 2));

        Image unmuteIcon = new Image(RESOURCES_PATH + "icons/unmute.png");
        Image muteIcon = new Image(RESOURCES_PATH + "icons/mute.png");
        ImageView muteIconView = new ImageView(unmuteIcon);
        muteIconView.setFitWidth(40);
        muteIconView.setFitHeight(40);

        Button muteButton = new Button();
        muteButton.setGraphic(muteIconView);
        muteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
        muteButton.setLayoutX(baseX);
        muteButton.setLayoutY(baseY + 2 * verticalSpacing);

        muteButton.setFocusTraversable(false);

        muteButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                boolean isMute = mediaPlayer.isMute();
                mediaPlayer.setMute(!isMute);
                muteIconView.setImage(isMute ? unmuteIcon : muteIcon);
            }
        });

        pane.getChildren().addAll(pauseIcon, pauseLabel, controlIcon, controlLabel, muteButton);
    }

    private void setupPauseOverlay() {
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setSpacing(20);

        ImageView pauseImage = new ImageView(new Image(RESOURCES_PATH + "img/parchemin.png"));
        pauseImage.setFitWidth(800);
        pauseImage.setFitHeight(800);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(10);
        dropShadow.setOffsetY(10);
        dropShadow.setColor(Color.color(0, 0, 0, 0.8));
        pauseImage.setEffect(dropShadow);

        Label pauseText = new Label("Jeu en Pause");
        pauseText.setFont(new Font("Arial", 20));
        pauseText.setTextFill(Color.BLACK);

        StackPane parcheminWithText = new StackPane(pauseImage, pauseText);
        StackPane.setAlignment(pauseText, Pos.TOP_CENTER);
        StackPane.setMargin(pauseText, new Insets(100, 0, 0, 0));
        VBox movementControlsBox = new VBox(10);
        movementControlsBox.setAlignment(Pos.TOP_CENTER);

        Label movementLabel = new Label("Déplacement");
        movementLabel.setFont(new Font("Arial", 12));
        movementLabel.setTextFill(Color.BLACK);

        HBox movementIcons = new HBox(5);
        movementIcons.setAlignment(Pos.CENTER);
        addControlIcon(movementIcons, RESOURCES_PATH + "icons/up_arrow.png", "");
        addControlIcon(movementIcons, RESOURCES_PATH + "icons/down_arrow.png", "");
        addControlIcon(movementIcons, RESOURCES_PATH + "icons/left_arrow.png", "");
        addControlIcon(movementIcons, RESOURCES_PATH + "icons/right_arrow.png", "");

        Label orLabel = new Label("Ou");
        orLabel.setFont(new Font("Arial", 10));
        orLabel.setTextFill(Color.BLACK);
        orLabel.setAlignment(Pos.CENTER);

        HBox zqsdIcons = new HBox(5);
        zqsdIcons.setAlignment(Pos.CENTER);
        addControlIcon(zqsdIcons, RESOURCES_PATH + "icons/lettre-z.png", "");
        addControlIcon(zqsdIcons, RESOURCES_PATH + "icons/lettre-s.png", "");
        addControlIcon(zqsdIcons, RESOURCES_PATH + "icons/lettre-q.png", "");
        addControlIcon(zqsdIcons, RESOURCES_PATH + "icons/lettre-d.png", "");

        movementControlsBox.getChildren().addAll(movementLabel, movementIcons, orLabel, zqsdIcons);

        VBox otherControlsBox = new VBox(10);
        otherControlsBox.setAlignment(Pos.TOP_CENTER);
        addControlIcon(otherControlsBox, RESOURCES_PATH + "icons/espace.png", "Tirer");
        addControlIcon(otherControlsBox, RESOURCES_PATH + "icons/lettre-b.png", "Défendre");
        addControlIcon(otherControlsBox, RESOURCES_PATH + "icons/lettre-c.png", "Changement de touche");
        addControlIcon(otherControlsBox, RESOURCES_PATH + "icons/lettre-p.png", "Pause/Reprise");
        addControlIcon(otherControlsBox, RESOURCES_PATH + "icons/echap.png", "Quitter");

        StackPane overlayContent = new StackPane();
        overlayContent.getChildren().addAll(parcheminWithText, movementControlsBox, otherControlsBox);

        StackPane.setAlignment(pauseText, Pos.TOP_CENTER);
        StackPane.setMargin(pauseText, new Insets(100, 0, 0, 0));
        StackPane.setMargin(movementControlsBox, new Insets(220, 0, 0, 0));
        StackPane.setMargin(otherControlsBox, new Insets(380, 0, 110, 0));

        pauseOverlay.getChildren().add(overlayContent);
        pauseOverlay.setVisible(false);

        root.getChildren().addAll(pane, pauseOverlay);
    }

    private void addControlIcon(Pane container, String iconPath, String description) {
        ImageView icon = new ImageView(new Image(iconPath));
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        Label label = new Label(description);
        label.setFont(new Font("Arial", 12));
        label.setTextFill(Color.BLACK);

        HBox hbox = new HBox(5, icon, label);
        hbox.setAlignment(Pos.CENTER);

        container.getChildren().add(hbox);
    }

    private void setupArena() {
        ImageView arenaImage = new ImageView(new Image(RESOURCES_PATH + "img/arena_rounded.png"));
        arenaImage.setFitWidth(1100);
        arenaImage.setFitHeight(650);
        arenaImage.setTranslateX((1350 - 1100) / 2);
        arenaImage.setTranslateY((800 - 650) / 2 + 10);

        DropShadow subtleShadow = new DropShadow();
        subtleShadow.setOffsetX(0);
        subtleShadow.setOffsetY(0);
        subtleShadow.setRadius(20);
        subtleShadow.setColor(Color.color(0, 0, 0, 0.5));

        DropShadow intenseShadow = new DropShadow();
        intenseShadow.setOffsetX(20);
        intenseShadow.setOffsetY(60);
        intenseShadow.setRadius(80);
        intenseShadow.setColor(Color.color(0, 0, 0, 0.7));

        arenaImage.setEffect(subtleShadow);
        Group arenaGroup = new Group(arenaImage);
        arenaGroup.setEffect(intenseShadow);

        pane.getChildren().add(arenaGroup);
    }

    private void setupPlayerHealthBar() {
        playerHealthBar = new HealthBar(180, 20, Color.DARKBLUE, Color.BLUE, true);
        playerHealthBar.setLayoutX(20);
        playerHealthBar.setLayoutY(20);

        Label playerLabel = new Label("Player");
        playerLabel.setLayoutX(20);
        playerLabel.setLayoutY(5);
        playerLabel.setTextFill(Color.WHITE);
        playerLabel.setFont(new Font("Arial", 12));

        pane.getChildren().addAll(player.getDragonView(), playerHealthBar, playerLabel);
    }

    private void setupOpponents() {
        for (int i = 0; i < opponents.size(); i++) {
            Opponent opponent = opponents.get(i);
            OpponentView opponentView = createOpponentView(opponent);
            opponentViews.add(opponentView);
            pane.getChildren().add(opponentView);
            opponentView.updateView();

            setupOpponentHealthBar(opponent, i);
        }
    }

    private OpponentView createOpponentView(Opponent opponent) {
        return (opponent instanceof Miniboss) ? new MinibossView((Miniboss) opponent) : new OpponentView(opponent);
    }

    private void setupOpponentHealthBar(Opponent opponent, int index) {
        double baseY = 20;
        double verticalSpacing = 30;

        HealthBar opponentHealthBar = new HealthBar(180, 20, Color.DARKRED, Color.RED, false);
        opponentHealthBar.setLayoutX(1160);
        opponentHealthBar.setLayoutY(baseY + index * verticalSpacing);

        opponentHealthBars.put(opponent, opponentHealthBar);
        pane.getChildren().addAll(opponentHealthBar);
    }

    private void setupKeyHandlers() {
        pane.setOnKeyPressed(this::handleKeyPress);
        pane.setOnKeyReleased(this::handleKeyRelease);
        pane.setFocusTraversable(true);
    }

    private void setupGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (!isPaused) {
                    if (lastUpdate > 0) {
                        long elapsedTime = (now - lastUpdate) / 1_000_000;
                        updateGame(elapsedTime);
                    }
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void updateGame(long elapsedTime) {
        if (isGameOver) {
            return;
        }

        updatePlayerMovement();
        updateProjectiles();
        updateOpponents(elapsedTime);
        updateHealthBars();
        checkGameOver();
    }

    private void updateOpponents(long elapsedTime) {
        for (int i = 0; i < opponents.size(); i++) {
            Opponent opponent = opponents.get(i);
            OpponentView opponentView = opponentViews.get(i);
            opponent.update(1300, 800, elapsedTime, player.getDragon(), projectileView, opponentView);
            opponentView.updateView();

            if (opponent.getHealth() <= 0) {
                removeOpponent(opponent, opponentView, i);
                i--;
            }
        }
    }

    private void removeOpponent(Opponent opponent, OpponentView opponentView, int index) {
        pane.getChildren().removeAll(opponentView, opponentHealthBars.get(opponent));
        opponents.remove(index);
        opponentViews.remove(index);
        opponentHealthBars.remove(opponent);
    }

    private void updateHealthBars() {
        for (Opponent opponent : opponents) {
            double opponentHealthPercentage = opponent.getHealth() / (double) opponent.getMaxHealth();
            HealthBar opponentHealthBar = opponentHealthBars.get(opponent);
            if (opponentHealthBar != null) {
                opponentHealthBar.setHealthPercentage(opponentHealthPercentage);
            }
        }

        double playerHealthPercentage = player.getDragon().getHealth() / (double) player.getDragon().getMaxHealth();
        playerHealthBar.setHealthPercentage(playerHealthPercentage);
    }

    private void checkGameOver() {
        if (isGameOver) {
            return;
        }

        if (player.getDragon().getHealth() <= 0) {
            isGameOver = true;
            endGame("Opponent");
        } else if (opponents.isEmpty()) {
            isGameOver = true;
            endGame("Player");
        }
    }

    private void handleKeyPress(KeyEvent event) {
        keysPressed.add(event.getCode());
        if (event.getCode() == KeyCode.P) {
            togglePause();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            handleQuit();
        } else if (event.getCode() == KeyCode.C) {
            toggleControlMode();
        }
    }

    private void handleQuit() {
        isPaused = true;

        Alert confirmExit = new Alert(Alert.AlertType.CONFIRMATION);
        confirmExit.setTitle("Confirmation");
        confirmExit.setHeaderText("Êtes-vous sûr de vouloir quitter le combat ?");

        Optional<ButtonType> result = confirmExit.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Le joueur a quitté le combat.");
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Arrêter la musique avant de changer de scène
            }
            returnToMainView();
        } else {
            isPaused = false;
        }
    }

    private void returnToMainView() {
        MainView mainView = new MainView(primaryStage, game, gameState);
        Scene mainScene = new Scene(mainView, 1350, 800);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void handleKeyRelease(KeyEvent event) {
        keysPressed.remove(event.getCode());
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseOverlay.setVisible(isPaused);
        System.out.println(isPaused ? "Pause" : "Reprise");
    }

    private boolean willCollideWithAnyOpponent(double deltaX, double deltaY) {
        double newX = player.getDragonView().getX() + deltaX;
        double newY = player.getDragonView().getY() + deltaY;
        double collisionMargin = 10;

        for (OpponentView opponentView : opponentViews) {
            if (player.getDragon().willCollide(
                    newX, newY,
                    player.getDragonView().getImage().getWidth(),
                    player.getDragonView().getImage().getHeight(),
                    opponentView.getX(), opponentView.getY(),
                    opponentView.getBoundsInLocal().getWidth(),
                    opponentView.getBoundsInLocal().getHeight(),
                    collisionMargin)) {
                return true;
            }
        }
        return false;
    }

    private void updatePlayerMovement() {
        handleDefense();
        handleMovement();
        handleShooting();
    }

    private void handleDefense() {
        if (keysPressed.contains(KeyCode.B)) {
            double detectionRadius = 100.0;
            if (projectileView.isProjectileNearby(player.getDragonView(), detectionRadius)) {
                player.defend();
            }
        }
    }

    private void handleMovement() {
        if (isZQSDControl) {
            if (keysPressed.contains(KeyCode.Z) && canMoveUp()) {
                player.moveUp();
            }
            if (keysPressed.contains(KeyCode.S) && canMoveDown()) {
                player.moveDown();
            }
            if (keysPressed.contains(KeyCode.Q) && canMoveLeft()) {
                player.moveLeft();
            }
            if (keysPressed.contains(KeyCode.D) && canMoveRight()) {
                player.moveRight();
            }
        } else {
            if (keysPressed.contains(KeyCode.UP) && canMoveUp()) {
                player.moveUp();
            }
            if (keysPressed.contains(KeyCode.DOWN) && canMoveDown()) {
                player.moveDown();
            }
            if (keysPressed.contains(KeyCode.LEFT) && canMoveLeft()) {
                player.moveLeft();
            }
            if (keysPressed.contains(KeyCode.RIGHT) && canMoveRight()) {
                player.moveRight();
            }
        }
    }

    private boolean canMoveUp() {
        return player.getDragonView().getY() > 0 && !willCollideWithAnyOpponent(0, -player.getDragon().getSpeed());
    }

    private boolean canMoveDown() {
        return player.getDragonView().getY() + player.getDragonView().getImage().getHeight() < pane.getHeight()
                && !willCollideWithAnyOpponent(0, player.getDragon().getSpeed());
    }

    private boolean canMoveLeft() {
        return player.getDragonView().getX() > 0 && !willCollideWithAnyOpponent(-player.getDragon().getSpeed(), 0);
    }

    private boolean canMoveRight() {
        return player.getDragonView().getX() + player.getDragonView().getImage().getWidth() < pane.getWidth()
                && !willCollideWithAnyOpponent(player.getDragon().getSpeed(), 0);
    }

    private void handleShooting() {
        if (keysPressed.contains(KeyCode.SPACE)) {
            long currentTime = System.nanoTime();
            if ((currentTime - lastShootTime) >= SHOOT_INTERVAL_SECONDS * 1_000_000_000) {
                projectileView.tirerProjectile(player.getDragon(), player.getDragon().getDirection(), true);
                lastShootTime = currentTime;
            }
        }
    }

    private void updateProjectiles() {
        projectileView.updateProjectiles(player.getDragonView(), opponentViews, player);
        projectileView.updateProjectilesFromOpponents(opponentViews, player.getDragonView(), player);
    }

    private void endGame(String winner) {
        boolean playerWon = winner.equals("Player");
        System.out.println("Le joueur a " + (playerWon ? "gagné" : "perdu") + " le niveau " + levelNumber);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (playerWon && levelNumber == 5) {
            System.out.println("Vous avez vaincu le mini boss");
            gameState.setEggs(gameState.getEggs() + 1);
        }
        if (playerWon) {
            gameState.markLevelAsCompleted(levelNumber);
            gameState.setCoins(gameState.getCoins() + 70);
        }

        GameView gameView = new GameView(primaryStage, this, levelNumber, game, gameState);
        gameView.showEndScreen(playerWon);
        Scene scene = new Scene(gameView, 1350, 800);
        primaryStage.setScene(scene);
    }

    public void clearGameElements() {
        pane.getChildren().clear();
    }

    private void playSound() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            String soundPath = Paths.get("src/ressources/song/combat1.mp3").toUri().toString();
            Media sound = new Media(soundPath);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du son : " + e.getMessage());
        }
    }

    private void toggleControlMode() {
        isZQSDControl = !isZQSDControl;
        System.out.println(isZQSDControl ? "Mode ZQSD activé" : "Mode Flèches activé");

        if (controlLabel != null) {
            controlLabel.setText((isZQSDControl ? "ZQSD" : "Flèches"));
        }
    }

}