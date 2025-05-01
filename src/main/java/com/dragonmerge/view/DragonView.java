package com.dragonmerge.view;

import com.dragonmerge.model.Dragon;
import com.dragonmerge.model.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Visual representation of a Dragon in the game.
 * It manages the images associated with different movement directions and
 * updates the dragon's position on screen.
 */
public class DragonView extends ImageView {

    /** The dragon model associated with this view. */
    private Dragon dragon;

    /** Path to the resources directory for images. */
    private static final String RESOURCES_PATH = "file:src/ressources/";

    private Image upImage = new Image(RESOURCES_PATH + "gif/gif_up.gif");
    private Image downImage = new Image(RESOURCES_PATH + "gif/gif_down.gif");
    private Image leftImage = new Image(RESOURCES_PATH + "gif/gif_left.gif");
    private Image rightImage = new Image(RESOURCES_PATH + "gif/gif_right.gif");

    /**
     * Constructs a DragonView with a specified Dragon model.
     *
     * @param dragon The dragon model to associate with this view.
     */
    public DragonView(Dragon dragon) {
        this.dragon = dragon;
        setX(dragon.getPositionX());
        setY(dragon.getPositionY());
        setImage(rightImage);

        setFitWidth(100);
        setFitHeight(100);
    }

    /**
     * Updates the view's position and image based on the dragon's current
     * direction.
     */
    public void updateView() {
        setX(dragon.getPositionX());
        setY(dragon.getPositionY());

        Direction direction = dragon.getDirection();
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
     * Resizes the dragon's image dimensions.
     *
     * @param width  The new width of the dragon image.
     * @param height The new height of the dragon image.
     */
    public void resizeDragon(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }
}
