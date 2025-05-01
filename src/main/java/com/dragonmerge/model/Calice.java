package com.dragonmerge.model;

import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.application.Platform;
import com.dragonmerge.Main;

/**
 * The `Calice` class handles the player's calices, including their quantity,
 * consumption,
 * and automatic recharging over time. It integrates with the game state to
 * ensure
 * synchronization of calice-related data.
 */
public class Calice {
    private IntegerProperty quantity;
    private static final int MAX_CALICES = 8;
    private static final long RECHARGE_INTERVAL = 15 * 60 * 1000; // 15min

    public Calice() {
        this.quantity = new SimpleIntegerProperty(0);
        startRechargeTimer();
    }

    public void initializeFromGameState(GameState gameState) {
        if (gameState != null) {
            this.quantity.set(gameState.getCalices());
        }
    }

    public boolean consume(int amount) {
        if (quantity.get() >= amount) {
            quantity.set(quantity.get() - amount);
            updateGameState();
            return true;
        }
        System.out.println("Calices insuffisants.");
        return false;
    }

    private void startRechargeTimer() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (quantity.get() < MAX_CALICES) {
                    Platform.runLater(() -> {
                        quantity.set(quantity.get() + 1);
                        updateGameState();
                    });
                }
            }
        }, RECHARGE_INTERVAL, RECHARGE_INTERVAL);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(Math.min(quantity, MAX_CALICES));
        updateGameState();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public boolean isRechargeable() {
        return quantity.get() < MAX_CALICES;
    }

    private void updateGameState() {
        GameState gameState = Main.getGameState();
        gameState.setCalices(quantity.get());
        gameState.setLastCaliceUpdate(Instant.now().toEpochMilli());
    }
}