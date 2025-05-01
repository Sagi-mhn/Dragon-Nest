package com.dragonmerge.tests;

import com.dragonmerge.utils.*;
import com.dragonmerge.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OpponentTest {

    private Opponent opponent;
    private Dragon playerDragon;

    @BeforeAll
    public static void initToolkit() throws Exception {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    public void setUp() {
        opponent = new Opponent(3.0, 100);

        playerDragon = new Dragon(100, 100, 5.0, 100, 1);
    }

    @Test
    public void testInitialSetup() {
        assertEquals(3.0, opponent.getSpeed(), "La vitesse de l'adversaire doit être correctement initialisé");
        assertEquals(100, opponent.getHealth(),
                "Les points de vie de l'adversaire doivent être correctement initialisé");
        assertEquals(Direction.LEFT, opponent.getDirection(), "L'adversaire devrait se diriger à gauche");
    }

    @Test
    public void testMovementReset() {
        opponent.resetMovement();
        assertTrue(opponent.getWidth() > 0, "La largeur de l'adversaire doit être définie");
        assertTrue(opponent.getHeight() > 0, "La hauteur de l'adversaire doit être définie");
    }

    @Test
    public void testAttack() {
        Dragon target = new Dragon(200, 200, 5.0, 100, 1);
        opponent.attack(target);
        assertEquals(90, target.getHealth(),
                "L'attaque de l'adversaire doit réduire la santé de la cible de 10 points");
    }

    @Test
    public void testDefend() {
        int initialHealth = opponent.getHealth();
        opponent.defend(20);
        assertEquals(initialHealth - 10, opponent.getHealth(), "La défense doit réduire les dégâts de moitié");
    }

}
