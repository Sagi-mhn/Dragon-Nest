package com.dragonmerge.tests;

import com.dragonmerge.model.*;
import com.dragonmerge.utils.GameSaveManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game(null);
    }

    @Test
    public void testInitializationWithoutGameState() {
        assertNotNull(game.getPlayer(), "Le joueur doit être initialisé");
        assertEquals(0, game.getCurrentLevelIndex(), "Le jeu doit commencer au niveau 0");
        for (int i = 1; i <= 5; i++) {
            assertFalse(game.isLevelCompleted(i), "Les niveaux ne doivent pas être complétés au début");
        }
    }

    @Test
    public void testInitializationWithGameState() {
        GameState gameState = new GameState();
        gameState.setCurrentLevel(3);
        List<Integer> completedLevels = new ArrayList<>();
        completedLevels.add(1);
        completedLevels.add(2);
        gameState.setCompletedLevels(completedLevels);

        game = new Game(gameState);

        assertEquals(2, game.getCurrentLevelIndex(), "Le niveau actuel doit être chargé correctement");
        assertTrue(game.isLevelCompleted(1), "Le niveau 1 doit être complété");
        assertTrue(game.isLevelCompleted(2), "Le niveau 2 doit être complété");
        assertFalse(game.isLevelCompleted(3), "Le niveau 3 ne doit pas être complété");
    }

    @Test
    public void testStartGame() {
        game.startGame();
        assertTrue(game.getPlayer().getDragon().isAlive(),
                "Le dragon du joueur doit être en vie après le début du jeu");
        assertTrue(game.getPlayer().getInventory().getCalices() >= 0, "Le joueur doit avoir un inventaire valide");
    }

    @Test
    public void testCompleteLevel() {
        game.completeLevel();
        assertTrue(game.isLevelCompleted(1), "Le premier niveau doit être marqué comme complété");
    }

    @Test
    public void testEndGame() {
        game.endGame();
        assertFalse(game.isLevelCompleted(5),
                "Tous les niveaux ne doivent pas être complétés si le jeu s'arrête prématurément");
    }

    @Test
    public void testSetLevelCompleted() {
        game.setLevelCompleted(3, true);
        assertTrue(game.isLevelCompleted(3), "Le niveau 3 doit être marqué comme complété");
        game.setLevelCompleted(3, false);
        assertFalse(game.isLevelCompleted(3), "Le niveau 3 ne doit plus être complété");
    }

    @Test
    public void testProgression() {
        game.setLevelCompleted(1, true);
        game.setLevelCompleted(2, true);
        game.setLevelCompleted(3, true);
        game.setCurrentLevelIndex(4);

        assertEquals(4, game.getCurrentLevelIndex(), "L'index du niveau actuel doit refléter la progression du joueur");
    }
}
