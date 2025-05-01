package com.dragonmerge.model;

import com.dragonmerge.view.ProjectileView;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a miniboss opponent in the game, a specialized version of the
 * opponent
 * capable of shooting multiple projectiles simultaneously.
 */
public class Miniboss extends Opponent {

    private int numberOfProjectiles;
    private boolean isBoss;

    /**
     * Constructs a Miniboss instance with specific speed, health, and number of
     * projectiles.
     *
     * @param speed               The movement speed of the miniboss.
     * @param health              The health points of the miniboss.
     * @param numberOfProjectiles The number of projectiles the miniboss can shoot
     *                            at once.
     */
    public Miniboss(double speed, int health, int numberOfProjectiles) {
        super(speed, health);
        this.numberOfProjectiles = numberOfProjectiles;
        this.isBoss = true;
    }

    /**
     * Overrides the shoot behavior of the opponent to fire multiple projectiles
     * in the same direction, creating a spread effect.
     *
     * @param projectileView The projectile view to handle the shooting logic.
     * @param dragon         The player's dragon character, used to calculate
     *                       shooting direction.
     */
    @Override
    protected void shoot(ProjectileView projectileView, Dragon dragon) {
        Direction shootDirection = calculateDirectionToDragon(dragon);

        if (shootDirection != null) {
            setDirection(shootDirection);

            double baseX = getPositionX() + getWidth() / 2;
            double baseY = getPositionY() + getHeight() / 2;

            double offset = 15;
            double x1, y1, x2, y2;

            if (shootDirection == Direction.LEFT || shootDirection == Direction.RIGHT) {
                x1 = baseX;
                y1 = baseY - offset;
                x2 = baseX;
                y2 = baseY + offset;
            } else if (shootDirection == Direction.UP || shootDirection == Direction.DOWN) {
                x1 = baseX - offset;
                y1 = baseY;
                x2 = baseX + offset;
                y2 = baseY;
            } else {
                x1 = baseX - offset;
                y1 = baseY - offset;
                x2 = baseX + offset;
                y2 = baseY + offset;
            }

            projectileView.tirerProjectile(this, x1, y1, shootDirection, false);

            projectileView.tirerProjectile(this, x2, y2, shootDirection, false);
        }
    }

}
