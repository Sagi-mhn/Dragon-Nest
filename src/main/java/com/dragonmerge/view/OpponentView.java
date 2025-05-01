package com.dragonmerge.view;

import com.dragonmerge.model.Opponent;
import com.dragonmerge.model.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the visual representation of an Opponent in the game.
 * This class extends ImageView and manages the graphical display and updates of
 * opponents' positions and directions.
 */
public class OpponentView extends ImageView {

    private Opponent opponent;

    /** Path to the resources directory for opponent images. */
    private static final String RESOURCES_PATH = "file:src/ressources/";

    private Image upImage = new Image(RESOURCES_PATH + "gif/red_up.gif");
    private Image downImage = new Image(RESOURCES_PATH + "gif/red_down.gif");
    private Image leftImage = new Image(RESOURCES_PATH + "gif/red_left.gif");
    private Image rightImage = new Image(RESOURCES_PATH + "gif/red_right.gif");

    /**
     * Constructs a new OpponentView with a specified Opponent model.
     *
     * @param opponent The opponent model to associate with this view.
     */
    public OpponentView(Opponent opponent) {
        this.opponent = opponent;

        setImage(leftImage);

        setFitWidth(100);
        setFitHeight(100);
    }

    /**
     * Updates the view's position and image based on the opponent's current
     * direction.
     */
    public void updateView() {
        setX(opponent.getPositionX());
        setY(opponent.getPositionY());

        Direction direction = opponent.getDirection();
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
     * Gets the opponent model associated with this view.
     *
     * @return The Opponent model.
     */
    public Opponent getOpponentModel() {
        return opponent;
    }
}
