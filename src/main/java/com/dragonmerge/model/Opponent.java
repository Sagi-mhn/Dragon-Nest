package com.dragonmerge.model;

import java.util.Random;
import com.dragonmerge.view.*;

/**
 * Represents an opponent in the game arena, capable of movement, shooting, and
 * defending.
 */
public class Opponent extends Character {
    private static final double OPPONENT_WIDTH = 50;
    private static final double OPPONENT_HEIGHT = 50;
    private static final double SPEED = 3.0;
    private static final long DEFENSE_COOLDOWN = 1000;

    private Random random = new Random();
    private boolean isMoving = false;
    private long pauseTime = 0;
    private long moveTime = 0;
    private int directionX = 0;
    private int directionY = 0;
    private long lastDefendTime = 0;

    private long lastShootTime = 0;
    private long nextShootInterval = 0;
    private static final long SHOOT_INTERVAL_MIN = 700; // Intervalle minimum
    private static final long SHOOT_INTERVAL_MAX = 1500; // Intervalle maximum
    private static final long SHOOT_PAUSE_DURATION = 400; // Durée de la pause

    /**
     * Constructs an opponent with the specified speed and health.
     *
     * @param speed  The movement speed of the opponent.
     * @param health The health points of the opponent.
     */
    public Opponent(double speed, int health) {
        super(
                Character.ARENA_X + Character.ARENA_WIDTH - OPPONENT_WIDTH - 50,
                Character.ARENA_Y + (Character.ARENA_HEIGHT - OPPONENT_HEIGHT) / 2,
                speed,
                health);

        resetMovement();
        scheduleNextShoot();
        setDirection(Direction.LEFT);
    }

    @Override
    public double getWidth() {
        return OPPONENT_WIDTH;
    }

    @Override
    public double getHeight() {
        return OPPONENT_HEIGHT;
    }

    /**
     * Resets the movement pattern of the opponent, determining new directions and
     * timing.
     */
    public void resetMovement() {
        pauseTime = random.nextInt(500) + 500;
        moveTime = random.nextInt(2000) + 1000;
        directionX = random.nextInt(3) - 1;
        directionY = random.nextInt(3) - 1;

        if (directionX == 0 && directionY == 0) {
            if (random.nextBoolean()) {
                directionX = random.nextInt(2) * 2 - 1;
            } else {
                directionY = random.nextInt(2) * 2 - 1;
            }
        }

        isMoving = false;
    }

    private void scheduleNextShoot() {
        nextShootInterval = SHOOT_INTERVAL_MIN + random.nextInt((int) (SHOOT_INTERVAL_MAX - SHOOT_INTERVAL_MIN));
    }

    /**
     * Updates the opponent's state, including movement, shooting, and defense
     * behavior.
     *
     * @param windowWidth    The width of the game window.
     * @param windowHeight   The height of the game window.
     * @param elapsedTime    The elapsed time since the last update.
     * @param dragon         The player's dragon character.
     * @param projectileView The projectile view for handling shooting logic.
     * @param opponentView   The view of the opponent for collision detection.
     */
    public void update(double windowWidth, double windowHeight, long elapsedTime, Dragon dragon,
            ProjectileView projectileView, OpponentView opponentView) {

        if (isMoving) {
            moveTime -= elapsedTime;
            if (moveTime <= 0) {
                isMoving = false;
                pauseTime = random.nextInt(500) + 500;
            } else {
                move(directionX, directionY, dragon, windowWidth, windowHeight);
            }
        } else {
            pauseTime -= elapsedTime;
            if (pauseTime <= 0) {
                resetMovement();
                isMoving = true;
            }
        }

        // Shooting management with temporary stop
        lastShootTime += elapsedTime;
        if (lastShootTime >= nextShootInterval) {
            // Stops the opponent to shoot
            isMoving = false;
            shoot(projectileView, dragon);
            lastShootTime = 0;
            scheduleNextShoot();

            pauseTime = SHOOT_PAUSE_DURATION;
        }

        double detectionRadius = 100.0;
        if (projectileView.isPlayerProjectileNearby(opponentView, detectionRadius)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDefendTime >= DEFENSE_COOLDOWN) {
                int actionChance = random.nextInt(100);

                if (actionChance < 30) {
                    performDodge(windowWidth, windowHeight);
                } else {
                    defend(10);
                }
                lastDefendTime = currentTime;
            }
        }
    }

    private void performDodge(double windowWidth, double windowHeight) {
        double dodgeDistance = SPEED;
        int dodgeDirection = random.nextInt(4);

        switch (dodgeDirection) {
            case 0:
                if (getPositionY() - dodgeDistance > 0) {
                    setPositionY(getPositionY() - dodgeDistance);
                }
                break;
            case 1:
                if (getPositionY() + dodgeDistance < windowHeight) {
                    setPositionY(getPositionY() + dodgeDistance);
                }
                break;
            case 2:
                if (getPositionX() - dodgeDistance > 0) {
                    setPositionX(getPositionX() - dodgeDistance);
                }
                break;
            case 3:
                if (getPositionX() + dodgeDistance < windowWidth) {
                    setPositionX(getPositionX() + dodgeDistance);
                }
                break;
        }
    }

    /**
     * Shoots a projectile in the direction of the player's dragon.
     *
     * @param projectileView The projectile view for handling shooting.
     * @param dragon         The player's dragon character.
     */
    protected void shoot(ProjectileView projectileView, Dragon dragon) {
        Direction shootDirection = calculateDirectionToDragon(dragon);

        if (shootDirection != null) {
            setDirection(shootDirection);
            projectileView.tirerProjectile(this, shootDirection, false);
        }
    }

    /**
     * Calculates the direction to the player's dragon based on relative positions.
     *
     * @param dragon The player's dragon character.
     * @return The direction towards the dragon.
     */
    protected Direction calculateDirectionToDragon(Dragon dragon) {
        double dx = dragon.getPositionX() - getPositionX();
        double dy = dragon.getPositionY() - getPositionY();

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }

    private Direction getCurrentMovementDirection() {
        if (directionX == -1) {
            return Direction.LEFT;
        } else if (directionX == 1) {
            return Direction.RIGHT;
        } else if (directionY == -1) {
            return Direction.UP;
        } else if (directionY == 1) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }

    private void move(int dx, int dy, Dragon dragon, double windowWidth, double windowHeight) {
        double newX = getPositionX() + dx * SPEED;
        double newY = getPositionY() + dy * SPEED;

        boolean moved = false;

        if (dx == -1 && newX >= ARENA_X && !willCollideWithDragon(-SPEED, 0, dragon)) {
            setPositionX(newX);
            moved = true;
        } else if (dx == -1) {
            directionX = 1;
        }

        if (dx == 1 && newX + OPPONENT_WIDTH <= ARENA_X + ARENA_WIDTH && !willCollideWithDragon(SPEED, 0, dragon)) {
            setPositionX(newX);
            moved = true;
        } else if (dx == 1) {
            directionX = -1;
        }

        if (dy == -1 && newY >= ARENA_Y && !willCollideWithDragon(0, -SPEED, dragon)) {
            setPositionY(newY);
            moved = true;
        } else if (dy == -1) {
            directionY = 1;
        }

        if (dy == 1 && newY + OPPONENT_HEIGHT <= ARENA_Y + ARENA_HEIGHT && !willCollideWithDragon(0, SPEED, dragon)) {
            setPositionY(newY);
            moved = true;
        } else if (dy == 1) {
            directionY = -1;
        }

        if (moved) {
            setDirection(getCurrentMovementDirection());
        }
    }

    private boolean willCollideWithDragon(double deltaX, double deltaY, Dragon dragon) {
        double newX = getPositionX() + deltaX;
        double newY = getPositionY() + deltaY;
        double collisionMargin = 10;

        return willCollide(newX, newY, getWidth(), getHeight(),
                dragon.getPositionX(), dragon.getPositionY(),
                dragon.getWidth(), dragon.getHeight(), collisionMargin);
    }

    @Override
    public void attack(Character target) {
        if (target.isAlive()) {
            int damage = 10;
            target.setHealth(target.getHealth() - damage);
        }
    }

    @Override
    public void defend(int damage) {
        int reducedDamage = damage / 2;
        setHealth(getHealth() - reducedDamage);
    }
}
