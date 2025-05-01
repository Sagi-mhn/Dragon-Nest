package com.dragonmerge.model;

public abstract class Character {

    private double positionX;
    private double positionY;
    private double speed;

    public abstract double getWidth();

    public abstract double getHeight();

    private Direction direction;
    private int health;
    private final int maxHealth;

    protected static final double ARENA_X = 150;
    protected static final double ARENA_Y = 90;
    protected static final double ARENA_WIDTH = 1000;
    protected static final double ARENA_HEIGHT = 580;

    public Character(double positionX, double positionY, double speed, int health) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speed = speed;
        this.health = health;
        this.maxHealth = health;
        this.direction = Direction.LEFT;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean willCollide(double x1, double y1, double width1, double height1,
            double x2, double y2, double width2, double height2,
            double collisionMargin) {
        double right1 = x1 + width1 + collisionMargin;
        double bottom1 = y1 + height1 + collisionMargin;
        double left2 = x2 - collisionMargin;
        double top2 = y2 - collisionMargin;
        double right2 = left2 + width2 + collisionMargin * 2;
        double bottom2 = top2 + height2 + collisionMargin * 2;

        return right1 > left2 && x1 < right2 && bottom1 > top2 && y1 < bottom2;
    }

    public void moveUp() {
        if (this.positionY - speed >= ARENA_Y) {
            this.positionY -= speed;
            this.direction = Direction.UP;
        }
    }

    public void moveDown() {
        if (this.positionY + speed + getHeight() <= ARENA_Y + ARENA_HEIGHT) {
            this.positionY += speed;
            this.direction = Direction.DOWN;
        }
    }

    public void moveLeft() {
        if (this.positionX - speed >= ARENA_X) {
            this.positionX -= speed;
            this.direction = Direction.LEFT;
        }
    }

    public void moveRight() {
        if (this.positionX + speed + getWidth() <= ARENA_X + ARENA_WIDTH) {
            this.positionX += speed;
            this.direction = Direction.RIGHT;
        }
    }

    public abstract void attack(Character target);

    public void defend(int damage) {
        this.health -= damage / 2;
        if (this.health < 0)
            this.health = 0;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() {
        return this.health > 0;
    }
}
