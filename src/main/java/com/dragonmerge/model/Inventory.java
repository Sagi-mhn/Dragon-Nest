package com.dragonmerge.model;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private int coins;
    private Calice calice;
    private int eggs;
    private int dragonNests;
    private Map<Integer, Integer> dragonsByLevel;

    private static final int CALICE_COST = 70;
    private static final int EGG_COST = 130;
    private static final int DRAGON_NEST_COST = 370;

    public Inventory() {
        this.coins = 400;
        this.calice = new Calice();
        this.eggs = 0;
        this.dragonNests = 0;
        this.dragonsByLevel = new HashMap<>();
        this.dragonsByLevel.put(1, 0);
    }

    public boolean buyEgg() {
        if (coins >= EGG_COST) {
            coins -= EGG_COST;
            eggs++;
            return true;
        }
        return false;
    }

    public boolean buyDragonNest() {
        if (coins >= DRAGON_NEST_COST) {
            coins -= DRAGON_NEST_COST;
            dragonNests++;
            return true;
        }
        return false;
    }

    public boolean buyCalice() {
        if (coins >= CALICE_COST && calice.isRechargeable()) {
            coins -= CALICE_COST;
            calice.setQuantity(calice.getQuantity() + 1);
            return true;
        }
        return false;
    }

    public boolean useCalice(int amount) {
        return calice.consume(amount);
    }

    public boolean mergeEggs() {
        if (eggs >= 3) {
            eggs -= 3;
            dragonsByLevel.put(1, dragonsByLevel.getOrDefault(1, 0) + 1);
            return true;
        }
        return false;
    }

    public boolean mergeDragons(int level) {
        if (dragonsByLevel.getOrDefault(level, 0) >= 3) {
            dragonsByLevel.put(level, dragonsByLevel.get(level) - 3);
            dragonsByLevel.put(level + 1, dragonsByLevel.getOrDefault(level + 1, 0) + 1);
            return true;
        }
        return false;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public int getCalices() {
        return calice.getQuantity();
    }

    public int getEggs() {
        return eggs;
    }

    public int getDragonNests() {
        return dragonNests;
    }

    public int getDragonsByLevel(int level) {
        return dragonsByLevel.getOrDefault(level, 0);
    }

    public void restoreCalices(int calices) {
        this.calice.setQuantity(calices);
    }

    public void setCalices(int calices) {
        this.calice.setQuantity(calices);
    }
}