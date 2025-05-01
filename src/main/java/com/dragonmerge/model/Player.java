package com.dragonmerge.model;

import com.dragonmerge.view.DragonView;

public class Player {

    private Dragon dragon;
    private DragonView dragonView;
    private double speed;
    private Inventory inventory;

    public Player() {
        this.speed = 5.0;
        this.inventory = new Inventory();

        double startX = Character.ARENA_X + 50;
        double startY = Character.ARENA_Y + (Character.ARENA_HEIGHT - Dragon.DRAGON_HEIGHT) / 2;

        this.dragon = new Dragon(startX, startY, speed, 100, 1);
        this.dragonView = new DragonView(dragon);
        dragon.setDirection(Direction.RIGHT);
    }

    public boolean buyEgg() {
        return inventory.buyEgg();
    }

    public boolean mergeEggsToDragon() {
        boolean fusionReussie = inventory.mergeEggs();
        if (fusionReussie && dragon == null) {
            this.dragon = new Dragon(0, 0, speed, 100, 1);
            this.dragonView = new DragonView(dragon);
            System.out.println("Dragon de niveau 1 créé !");
        }
        return fusionReussie;
    }

    public boolean mergeDragons(int level) {
        return inventory.mergeDragons(level);
    }

    public void moveUp() {
        if (dragon != null) {
            dragon.moveUp();
            dragonView.updateView();
        }
    }

    public void moveDown() {
        if (dragon != null) {
            dragon.moveDown();
            dragonView.updateView();
        }
    }

    public void moveLeft() {
        if (dragon != null) {
            dragon.moveLeft();
            dragonView.updateView();
        }
    }

    public void moveRight() {
        if (dragon != null) {
            dragon.moveRight();
            dragonView.updateView();
        }
    }

    public void attack(Opponent opponent) {
        if (opponent != null && opponent.isAlive()) {
            int damage = 10;
            int newHealth = opponent.getHealth() - damage;
            opponent.setHealth(newHealth);
        }
    }

    public void defend() {
        if (dragon != null && dragon.isAlive()) {
            int selfDamage = 5;
            int newDragonHealth = dragon.getHealth() - selfDamage;
            dragon.setHealth(newDragonHealth);
            if (!dragon.isAlive()) {
                System.out.println("Le dragon du joueur est mort !");
            }
        } else {
            System.out.println("Pas de dragon pour se défendre ou le dragon est déjà mort.");
        }
    }

    public void resetDragon() {
        double startX = Character.ARENA_X + 50;
        double startY = Character.ARENA_Y + (Character.ARENA_HEIGHT - Dragon.DRAGON_HEIGHT) / 2;
        this.dragon = new Dragon(startX, startY, speed, 100, 1);
        this.dragonView = new DragonView(dragon);
        dragon.setDirection(Direction.RIGHT);
    }

    public boolean buyDragonNest() {
        return inventory.buyDragonNest();
    }

    public boolean buyCalice() {
        return inventory.buyCalice();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Dragon getDragon() {
        return dragon;
    }

    public DragonView getDragonView() {
        return dragonView;
    }
}
