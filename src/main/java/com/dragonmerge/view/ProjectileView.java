package com.dragonmerge.view;

import com.dragonmerge.model.*;
import com.dragonmerge.model.Character;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Manages the display and movement of projectiles in the game environment.
 */
public class ProjectileView {
    private List<IProjectile> playerProjectiles = new ArrayList<>();
    private List<IProjectile> opponentProjectiles = new ArrayList<>();

    private static final double PROJECTILE_SPEED = 7.0;
    private Pane pane;

    /**
     * Constructs a ProjectileView managing projectiles within a given pane.
     * 
     * @param pane The pane to display projectiles on.
     */
    public ProjectileView(Pane pane) {
        this.pane = pane;
    }

    /**
     * Fires a projectile from a given shooter character.
     * 
     * @param shooter   The character shooting the projectile.
     * @param x         The initial x position of the projectile.
     * @param y         The initial y position of the projectile.
     * @param direction The direction of the projectile.
     * @param isPlayer  True if the shooter is the player, false if an opponent.
     */
    public void tirerProjectile(Character shooter, double x, double y, Direction direction, boolean isPlayer) {
        Projectile projectile = new Projectile(x, y, direction.toString(), PROJECTILE_SPEED);
        if (isPlayer) {
            playerProjectiles.add(projectile);
        } else {
            opponentProjectiles.add(projectile);
        }
        pane.getChildren().add(projectile);
    }

    /**
     * Convenience method to fire a projectile directly from the character's current
     * position.
     * 
     * @param shooter   The character shooting the projectile.
     * @param direction The direction of the projectile.
     * @param isPlayer  True if the shooter is the player, false if an opponent.
     */
    public void tirerProjectile(Character shooter, Direction direction, boolean isPlayer) {
        double x = shooter.getPositionX() + shooter.getWidth() / 2;
        double y = shooter.getPositionY() + shooter.getHeight() / 2;
        tirerProjectile(shooter, x, y, direction, isPlayer);
    }

    /**
     * Moves a given projectile according to its direction and speed.
     * 
     * @param projectile The projectile to move.
     */
    private void moveProjectile(IProjectile projectile) {
        double dx = 0, dy = 0;
        double speed = projectile.getSpeed();
        switch (projectile.getDirection()) {
            case "UP":
                dy = -speed;
                break;
            case "DOWN":
                dy = speed;
                break;
            case "LEFT":
                dx = -speed;
                break;
            case "RIGHT":
                dx = speed;
                break;
            case "UP_LEFT":
                dx = -speed / Math.sqrt(2);
                dy = -speed / Math.sqrt(2);
                break;
            case "UP_RIGHT":
                dx = speed / Math.sqrt(2);
                dy = -speed / Math.sqrt(2);
                break;
            case "DOWN_LEFT":
                dx = -speed / Math.sqrt(2);
                dy = speed / Math.sqrt(2);
                break;
            case "DOWN_RIGHT":
                dx = speed / Math.sqrt(2);
                dy = speed / Math.sqrt(2);
                break;
        }
        projectile.move(dx, dy);
    }

    /**
     * Updates all projectiles aimed at a given target, checking for collisions.
     * 
     * @param projectiles The list of projectiles to update.
     * @param target      The target to check for collisions.
     * @param onHit       A consumer that processes what happens when the target is
     *                    hit.
     */
    private void updateProjectiles(List<IProjectile> projectiles, Object target, Consumer<Object> onHit) {
        Iterator<IProjectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            IProjectile projectile = iterator.next();
            moveProjectile(projectile);

            Projectile proj = (Projectile) projectile;
            if (proj.isOutOfBounds()) {
                iterator.remove();
                pane.getChildren().remove(proj);
                continue;
            }

            if (target instanceof DragonView
                    && proj.getBoundsInParent().intersects(((DragonView) target).getBoundsInParent())) {
                iterator.remove();
                pane.getChildren().remove(proj);
                onHit.accept(target);
            } else if (target instanceof OpponentView
                    && proj.getBoundsInParent().intersects(((OpponentView) target).getBoundsInParent())) {
                iterator.remove();
                pane.getChildren().remove(proj);
                onHit.accept(target);
            }
        }
    }

    /**
     * Updates projectiles launched by the player and checks for hits against
     * opponents.
     * 
     * @param playerDragonView The player's dragon view.
     * @param opponentViews    List of opponent views to check for hits.
     * @param playerModel      The player model to update based on hits.
     */
    public void updateProjectiles(DragonView playerDragonView, List<OpponentView> opponentViews, Player playerModel) {
        Iterator<IProjectile> iterator = playerProjectiles.iterator();
        while (iterator.hasNext()) {
            IProjectile projectile = iterator.next();
            moveProjectile(projectile);

            Projectile proj = (Projectile) projectile;
            if (proj.isOutOfBounds()) {
                iterator.remove();
                pane.getChildren().remove(proj);
                continue;
            }

            boolean hit = false;
            for (OpponentView opponentView : opponentViews) {
                if (proj.getBoundsInParent().intersects(opponentView.getBoundsInParent())) {
                    playerModel.attack(opponentView.getOpponentModel());
                    hit = true;
                    break;
                }
            }

            if (hit) {
                iterator.remove();
                pane.getChildren().remove(proj);
            }
        }
    }

    /**
     * Updates projectiles launched by opponents and checks for hits against the
     * player.
     * 
     * @param opponentViews    List of opponent views that launched projectiles.
     * @param playerDragonView The player's dragon view to check for hits.
     * @param playerModel      The player model to update based on hits.
     */
    public void updateProjectilesFromOpponents(List<OpponentView> opponentViews, DragonView playerDragonView,
            Player playerModel) {
        updateProjectiles(opponentProjectiles, playerDragonView, player -> {
            playerModel.getDragon().setHealth(playerModel.getDragon().getHealth() - 10);
        });
    }

    /**
     * Checks if any opponent projectile is within a certain radius of the player's
     * dragon.
     * 
     * @param playerDragon    The player's dragon view.
     * @param detectionRadius The radius to check around the player's dragon.
     * @return True if any projectile is within the radius, false otherwise.
     */
    public boolean isProjectileNearby(DragonView playerDragon, double detectionRadius) {
        for (IProjectile projectile : opponentProjectiles) {
            Projectile proj = (Projectile) projectile;
            double centerX = proj.getX() + proj.getBoundsInLocal().getWidth() / 2;
            double centerY = proj.getY() + proj.getBoundsInLocal().getHeight() / 2;
            double distance = Math.sqrt(
                    Math.pow(centerX - playerDragon.getX(), 2) +
                            Math.pow(centerY - playerDragon.getY(), 2));
            if (distance <= detectionRadius) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any player projectile is within a certain radius of an opponent.
     * 
     * @param opponentView    The opponent view.
     * @param detectionRadius The radius to check around the opponent.
     * @return True if any projectile is within the radius, false otherwise.
     */
    public boolean isPlayerProjectileNearby(OpponentView opponentView, double detectionRadius) {
        for (IProjectile projectile : playerProjectiles) {
            Projectile proj = (Projectile) projectile;
            double centerX = proj.getX() + proj.getBoundsInLocal().getWidth() / 2;
            double centerY = proj.getY() + proj.getBoundsInLocal().getHeight() / 2;
            double distance = Math.sqrt(
                    Math.pow(centerX - opponentView.getX(), 2) +
                            Math.pow(centerY - opponentView.getY(), 2));
            if (distance <= detectionRadius) {
                return true;
            }
        }
        return false;
    }
}