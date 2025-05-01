package com.dragonmerge.model;

import java.util.ArrayList;
import java.util.List;
import com.dragonmerge.utils.GameSaveManager;

/**
 * Represents the main game logic, managing levels, player progress, and
 * battles.
 */
public class Game {

    private Player player;
    private List<Level> levels;
    private int currentLevelIndex;
    private boolean gameInProgress;

    /**
     * Initializes a new game instance.
     *
     * @param gameState The current game state to load progress from, or null to
     *                  start a new game.
     */
    public Game(GameState gameState) {
        this.player = new Player();
        this.levels = new ArrayList<>();
        this.player = new Player();
        initializeLevels();
        if (gameState != null) {
            this.currentLevelIndex = gameState.getCurrentLevel() - 1;
            updateLevels(gameState.getCompletedLevels());
        } else {
            this.currentLevelIndex = 0;
            for (Level level : levels) {
                level.setCompleted(false);
            }
        }
        this.gameInProgress = false;
    }

    private void updateLevels(List<Integer> completedLevels) {
        if (completedLevels != null) {
            for (int i = 0; i < levels.size(); i++) {
                levels.get(i).setCompleted(completedLevels.contains(i + 1));
            }
        }
    }

    public void completeLevel() {
        levels.get(currentLevelIndex).setCompleted(true);
        GameState gameState = new GameState();
        gameState.setCurrentLevel(currentLevelIndex + 1);
        gameState.setCompletedLevels(getCompletedLevels());
        gameState.setCalices(player.getInventory().getCalices());
        GameSaveManager.saveGame(gameState);
    }

    private List<Integer> getCompletedLevels() {
        List<Integer> completed = new ArrayList<>();
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).isCompleted()) {
                completed.add(i + 1);
            }
        }
        return completed;
    }

    private void initializeLevels() {
        for (int i = 1; i <= 5; i++) {
            levels.add(new Level(i));
        }
    }

    public void startGame() {
        System.out.println("Bienvenue dans DragonNest!");
        this.gameInProgress = true;
        startLevel();
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void setCurrentLevelIndex(int currentLevelIndex) {
        this.currentLevelIndex = currentLevelIndex;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void startLevel() {
        if (currentLevelIndex < levels.size()) {
            Level currentLevel = levels.get(currentLevelIndex);
            System.out.println("Niveau " + currentLevel.getLevelNumber() + " commencé !");
            startBattle(currentLevel.getLevelNumber());
        } else {
            System.out.println("Félicitations ! Vous avez terminé tous les niveaux !");
            endGame();
        }
    }

    public void startBattle(int levelNumber) {
        if (player.getDragon() != null && player.getInventory().getCalices() > 0) {
            Battle battle = new Battle(player, levelNumber);
            boolean playerWon = battle.start();
            completeBattle(playerWon);
        } else {
            System.out.println("Vous ne pouvez pas démarrer un combat.");
        }
    }

    private void completeBattle(boolean playerWon) {
        Level currentLevel = levels.get(currentLevelIndex);
        if (playerWon) {
            System.out.println("Victoire ! Passage au niveau suivant.");
            currentLevel.setCompleted(true);
            completeLevel();
            if (currentLevelIndex < levels.size() - 1) {
                currentLevelIndex++;
            }
            startLevel();
        } else {
            System.out.println("Défaite... Réessayez le niveau " + currentLevel.getLevelNumber());
        }
    }

    public void endGame() {
        this.gameInProgress = false;
        System.out.println("Merci d'avoir joué à DragonNest!");
    }

    public boolean isLevelCompleted(int levelNumber) {
        for (Level level : levels) {
            if (level.getLevelNumber() == levelNumber) {
                return level.isCompleted();
            }
        }
        return false;
    }

    public void setLevelCompleted(int levelNumber, boolean completed) {
        for (Level level : levels) {
            if (level.getLevelNumber() == levelNumber) {
                level.setCompleted(completed);
            }
        }
    }
}
