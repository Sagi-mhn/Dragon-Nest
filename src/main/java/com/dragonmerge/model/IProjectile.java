package com.dragonmerge.model;

/**
 * Represents the behavior of a projectile in the game.
 * Defines methods for movement, direction, and speed of the projectile.
 */
public interface IProjectile {
    /**
     * Moves the projectile by the specified deltas along the X and Y axes.
     *
     * @param dx The change in the X-coordinate.
     * @param dy The change in the Y-coordinate.
     */
    void move(double dx, double dy);

    String getDirection();

    double getSpeed();
}