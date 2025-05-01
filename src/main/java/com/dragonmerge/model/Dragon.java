package com.dragonmerge.model;

/**
 * Represents a non-controllable dragon entity in the game.
 * These dragons act as independent characters with specific attributes and
 * behaviors.
 */
public class Dragon extends Character {
    protected static final double DRAGON_WIDTH = 50;
    protected static final double DRAGON_HEIGHT = 50;
    private int level;

    /**
     * Constructs a non-controllable Dragon instance with specified attributes.
     *
     * @param positionX The X-coordinate of the dragon's position.
     * @param positionY The Y-coordinate of the dragon's position.
     * @param speed     The movement speed of the dragon.
     * @param health    The health points of the dragon.
     * @param level     The level of the dragon.
     */
    public Dragon(double positionX, double positionY, double speed, int health, int level) {
        super(positionX, positionY, speed, health);
        this.level = level;
        setDirection(Direction.LEFT);
    }

    @Override
    public double getWidth() {
        return DRAGON_WIDTH;
    }

    @Override
    public double getHeight() {
        return DRAGON_HEIGHT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void attack(Character target) {
        target.setHealth(target.getHealth() - 10);
    }

    public boolean canBeMerged() {
        return this.level < 3;
    }
}
