package com.dragonmerge.tests;

import com.dragonmerge.model.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {

    private Level level;

    @BeforeEach
    public void setUp() {
        level = new Level(1);
    }

    @Test
    public void testLevelInitialization() {
        assertEquals(1, level.getLevelNumber(), "Leniveau doit être correctement initialisé");
        assertFalse(level.isCompleted(), "Un nouveau niveau ne doit pas être marqué comme complété");
    }

    @Test
    public void testSetCompleted() {
        level.setCompleted(true);
        assertTrue(level.isCompleted(), "Le niveau doit être marqué comme complété après appel de setCompleted(TRUE)");

        level.setCompleted(false);
        assertFalse(level.isCompleted(),
                "Le niveau ne doit plus être marqué comme complété après appel de setCompleted(FLSE)");
    }

    @Test
    public void testCanProgress() {
        assertTrue(level.canProgress(true), "Le joueur doit pouvoir progresser si le joueur a gagné");
        assertFalse(level.canProgress(false), "Le joueur ne doit pas pouvoir progresser si le joueur a perdu");
    }
}
