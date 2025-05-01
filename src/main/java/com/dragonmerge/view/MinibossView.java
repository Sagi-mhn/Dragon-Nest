package com.dragonmerge.view;

import com.dragonmerge.model.Miniboss;
import com.dragonmerge.model.Direction;
import javafx.scene.image.Image;

/**
 * Represents the visual representation of a Miniboss in the game.
 * Extends OpponentView to provide specific graphics and behaviors for
 * minibosses.
 */
public class MinibossView extends OpponentView {

    private Miniboss miniboss;
    /** Path to the resources directory for miniboss images. */
    private static final String RESOURCES_PATH = "file:src/ressources/";

    private Image upImage = new Image(RESOURCES_PATH + "gif/boss_up.gif");
    private Image downImage = new Image(RESOURCES_PATH + "gif/boss_down.gif");
    private Image leftImage = new Image(RESOURCES_PATH + "gif/boss_left.gif");
    private Image rightImage = new Image(RESOURCES_PATH + "gif/boss_right.gif");

    /**
     * Constructs a new MinibossView with a specified Miniboss model.
     *
     * @param miniboss The miniboss model to associate with this view.
     */
    public MinibossView(Miniboss miniboss) {
        super(miniboss);
        this.miniboss = miniboss;
        setImage(leftImage);
        updateView();

        setFitWidth(170);
        setFitHeight(170);
    }

    /**
     * Updates the view's position and image based on the miniboss's current
     * direction.
     */
    @Override
    public void updateView() {
        setX(miniboss.getPositionX());
        setY(miniboss.getPositionY());

        Direction direction = miniboss.getDirection();
        switch (direction) {
            case UP:
                setImage(upImage);
                break;
            case DOWN:
                setImage(downImage);
                break;
            case LEFT:
                setImage(leftImage);
                break;
            case RIGHT:
                setImage(rightImage);
                break;
        }
    }

    /**
     * Gets the miniboss model associated with this view.
     *
     * @return The Miniboss model.
     */
    public Miniboss getMinibossModel() {
        return miniboss;
    }
}