package com.dragonmerge.model;

import com.dragonmerge.model.Dragon;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the state of the game, tracking the player's resources, progress,
 * and actions.
 */
public class GameState {
    private int coins;
    private int calices;
    private int dragons;
    private int eggs;
    private int dragonNests;
    private List<Integer> completedLevels;
    private int currentLevel;
    private long lastCaliceUpdate;

    private static final int CALICE_COST = 70;
    private static final int EGG_COST = 130;
    private static final int DRAGON_NEST_COST = 360;

    public GameState() {
        calices = 8;
        completedLevels = new ArrayList<>();
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCalices() {
        return this.calices;
    }

    public void setCalices(int calices) {
        this.calices = calices;
    }

    public int getDragonNests() {
        return dragonNests;
    }

    public void setDragonNests(int dragonNests) {
        this.dragonNests = dragonNests;
    }

    public int getDragons() {
        return dragons;
    }

    public void setDragons(int dragons) {
        this.dragons = dragons;
    }

    public int getEggs() {
        return eggs;
    }

    public void setEggs(int eggs) {
        this.eggs = eggs;
    }

    public boolean mergeEggs() {
        if (eggs >= 3) {
            eggs -= 3;

            return true;
        }
        return false;
    }

    public List<Integer> getCompletedLevels() {
        return completedLevels;
    }

    public void setCompletedLevels(List<Integer> completedLevels) {
        this.completedLevels = completedLevels;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public long getLastCaliceUpdate() {
        return lastCaliceUpdate;
    }

    public void setLastCaliceUpdate(long lastCaliceUpdate) {
        this.lastCaliceUpdate = lastCaliceUpdate;
    }

    // SHOP
    public boolean buyEgg() {
        if (coins >= EGG_COST) {
            coins -= EGG_COST;
            eggs++;
            return true;
        }
        System.out.println("vous n'avez pas assez de pièces pour acheter un oeuf.");
        return false;
    }

    public boolean buyDragonNest() {
        if (coins >= DRAGON_NEST_COST) {
            coins -= DRAGON_NEST_COST;
            eggs += 3;
            return true;
        }
        System.out.println("Vous n'avez pas assez de pièces pour acheter un nid.");
        return false;
    }

    public boolean buyCalice() {
        if (coins >= CALICE_COST && calices < 8) {
            coins -= CALICE_COST;
            calices++;
            return true;
        } else if (calices == 8) {
            System.out.println("Vous possédez le nombre max de calices.");
            return false;
        }
        System.out.println("Vous n'avez pas assez de pièces pour acheter un calice.");
        return false;
    }

    public void markLevelAsCompleted(int level) {
        if (!completedLevels.contains(level)) {
            completedLevels.add(level);
        }
    }

}
