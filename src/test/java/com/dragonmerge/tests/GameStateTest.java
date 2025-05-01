package com.dragonmerge.tests;

import com.dragonmerge.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        System.out.println("Nouvel état de jeu initialisé.");
    }

    @Test
    void testInitialState() {
        System.out.println("Test de l'état initial...");
        assertEquals(8, gameState.getCalices());
        assertEquals(0, gameState.getCoins());
        assertEquals(0, gameState.getDragons());
        assertEquals(0, gameState.getEggs());
        assertEquals(0, gameState.getDragonNests());
        assertTrue(gameState.getCompletedLevels().isEmpty());
        System.out.println("Test de l'état initial réussi.");
    }

    @Test
    void testBuyEgg() {
        System.out.println("Test de l'achat d'un œuf...");
        gameState.setCoins(130);
        assertTrue(gameState.buyEgg());
        assertEquals(0, gameState.getCoins());
        assertEquals(1, gameState.getEggs());
        System.out.println(
                "Œuf acheté avec succès. Pièces : " + gameState.getCoins() + ", Oeuf : " + gameState.getEggs());
    }

    @Test
    void testCannotBuyEggWithoutCoins() {
        System.out.println("Test de l'impossibilité d'acheter un œuf sans pièces...");
        gameState.setCoins(0);
        assertFalse(gameState.buyEgg());
        assertEquals(0, gameState.getEggs());
        System.out.println("Impossible d'acheter un œuf comme prévu. Oeuf : " + gameState.getEggs());
    }

    @Test
    void testBuyDragonNest() {
        System.out.println("Test de l'achat d'un nid de dragon...");
        gameState.setCoins(360);
        assertTrue(gameState.buyDragonNest());
        assertEquals(0, gameState.getCoins());
        assertEquals(3, gameState.getEggs());
        System.out.println("Nid de dragon acheté avec succès. Pièces : " + gameState.getCoins() + ", Oeuf : "
                + gameState.getEggs());
    }

    @Test
    void testBuyCalice() {
        System.out.println("Test de l'achat d'un calice...");
        gameState.setCoins(70);
        gameState.setCalices(7);
        assertTrue(gameState.buyCalice());
        assertEquals(0, gameState.getCoins());
        assertEquals(8, gameState.getCalices());
        System.out.println("Calice acheté avec succès. Pièces : " + gameState.getCoins() + ", Calices : "
                + gameState.getCalices());
    }

    @Test
    void testCannotBuyCaliceWhenFull() {
        System.out.println("Test de l'impossibilité d'acheter un calice lorsque la limite est atteinte...");
        gameState.setCoins(70);
        gameState.setCalices(8);
        assertFalse(gameState.buyCalice());
        assertEquals(70, gameState.getCoins());
        assertEquals(8, gameState.getCalices());
        System.out.println("Impossible d'acheter un calice comme prévu. Pièces : " + gameState.getCoins()
                + ", Calices : " + gameState.getCalices());
    }
}
