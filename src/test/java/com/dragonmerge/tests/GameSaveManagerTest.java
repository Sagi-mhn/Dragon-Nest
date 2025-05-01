package com.dragonmerge.tests;

import com.dragonmerge.utils.*;
import com.dragonmerge.model.GameState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameSaveManagerTest {

    @Test
    void testSaveAndLoadGame() {
        System.out.println("Test de sauvegarde et chargement de la partie...");

        GameState originalState = new GameState();
        originalState.setCoins(1000);
        originalState.setCalices(5);
        originalState.setCurrentLevel(3);

        System.out.println("État initial : Pièces = " + originalState.getCoins()
                + ", Calices = " + originalState.getCalices()
                + ", Niveau actuel = " + originalState.getCurrentLevel());

        GameSaveManager.saveGame(originalState);
        System.out.println("Partie sauvegardée avec succès.");

        GameState loadedState = GameSaveManager.loadGame();
        System.out.println("Partie chargée avec succès.");

        assertNotNull(loadedState);
        assertEquals(originalState.getCoins(), loadedState.getCoins());
        assertEquals(originalState.getCalices(), loadedState.getCalices());
        assertEquals(originalState.getCurrentLevel(), loadedState.getCurrentLevel());

        System.out.println("Vérification des données chargées : Pièces = " + loadedState.getCoins()
                + ", Calices = " + loadedState.getCalices()
                + ", Niveau actuel = " + loadedState.getCurrentLevel());
        System.out.println("Test de sauvegarde et chargement réussi.");
    }

    @Test
    void testLoadExistingFile() {
        System.out.println("Test de chargement d'une sauvegarde existante...");

        GameState loadedState = GameSaveManager.loadGame();

        assertNotNull(loadedState);
        System.out.println("Sauvegarde existante chargée avec succès : Pièces = " + loadedState.getCoins()
                + ", Calices = " + loadedState.getCalices()
                + ", Niveau actuel = " + loadedState.getCurrentLevel());
    }
}
