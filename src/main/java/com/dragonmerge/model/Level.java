package com.dragonmerge.model;

/**
 * Represents a game level with a specific number and completion status.
 * Levels can be marked as completed and allow progression based on the player's
 * success.
 */
public class Level {

    private int levelNumber;
    private boolean completed;

    /**
     * Constructs a Level instance with a given level number.
     * Initially, the level is not completed.
     *
     * @param levelNumber The number representing the level.
     */
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.completed = false;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Checks if the level has been completed.
     *
     * @return True if the level is completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Determines if the player can progress to the next level based on their
     * performance.
     *
     * @param playerWon True if the player won the current level, false otherwise.
     * @return True if progression to the next level is allowed, false otherwise.
     */
    public boolean canProgress(boolean playerWon) {
        return playerWon;
    }
}
