package com.dragonmerge.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile extends ImageView implements IProjectile {
    private String direction;
    private double speed;

    private static final double ARENA_X = 150;
    private static final double ARENA_Y = 110;
    private static final double ARENA_WIDTH = 1050;
    private static final double ARENA_HEIGHT = 590;

    /** Path to the resources directory. */
    private static final String RESOURCES_PATH = "file:src/ressources/";

    public Projectile(double x, double y, String direction, double speed) {
        super(new Image(RESOURCES_PATH + "gif/fireball-fire.gif"));
        this.direction = direction;
        this.speed = speed;

        setFitWidth(35);
        setFitHeight(15);
        setX(x);
        setY(y);
        setRotation();
    }

    private void setRotation() {
        switch (direction) {
            case "UP":
                setRotate(270);
                break;
            case "DOWN":
                setRotate(90);
                break;
            case "LEFT":
                setRotate(180);
                break;
            case "RIGHT":
                setRotate(0);
                break;
            case "UP_LEFT":
                setRotate(225);
                break;
            case "UP_RIGHT":
                setRotate(315);
                break;
            case "DOWN_LEFT":
                setRotate(135);
                break;
            case "DOWN_RIGHT":
                setRotate(45);
                break;
        }
    }

    @Override
    public void move(double dx, double dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    public boolean isOutOfBounds() {
        double centerX = getX() + getBoundsInLocal().getWidth() / 2;
        double centerY = getY() + getBoundsInLocal().getHeight() / 2;

        return centerX < ARENA_X ||
                centerX > ARENA_X + ARENA_WIDTH ||
                centerY < ARENA_Y ||
                centerY > ARENA_Y + ARENA_HEIGHT;
    }

    @Override
    public String getDirection() {
        return direction;
    }

    @Override
    public double getSpeed() {
        return speed;
    }
}