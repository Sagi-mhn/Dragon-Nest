package com.dragonmerge.tests;

import com.dragonmerge.model.*;
import com.dragonmerge.model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DragonTest {

    private Dragon dragon;

    @BeforeEach
    void setUp() {
        dragon = new Dragon(200, 200, 5.0, 100, 1);
        System.out.println(
                "Configuration d'un nouveau Dragon à la position (200, 200) avec une vitesse de 5.0 et une santé de 100.");
    }

    @Test
    void testInitialState() {
        System.out.println("Test de l'état initial...");
        assertEquals(200, dragon.getPositionX());
        assertEquals(200, dragon.getPositionY());
        assertEquals(5.0, dragon.getSpeed());
        assertEquals(100, dragon.getHealth());
        assertEquals(1, dragon.getLevel());
        assertEquals(Direction.LEFT, dragon.getDirection());
        System.out.println("Test de l'état initial réussi.");
    }

    @Test
    void testMove() {
        System.out.println("Test des déplacements...");

        dragon.moveRight();
        assertEquals(205, dragon.getPositionX());
        assertEquals(Direction.RIGHT, dragon.getDirection());
        System.out.println("Déplacé à droite à la position : " + dragon.getPositionX());

        dragon.moveDown();
        assertEquals(205, dragon.getPositionY());
        assertEquals(Direction.DOWN, dragon.getDirection());
        System.out.println("Déplacé en bas à la position : " + dragon.getPositionY());

        dragon.moveLeft();
        assertEquals(200, dragon.getPositionX());
        assertEquals(Direction.LEFT, dragon.getDirection());
        System.out.println("Déplacé à gauche à la position : " + dragon.getPositionX());

        dragon.moveUp();
        assertEquals(200, dragon.getPositionY());
        assertEquals(Direction.UP, dragon.getDirection());
        System.out.println("Déplacé en haut à la position : " + dragon.getPositionY());

        System.out.println("Test des déplacements réussi.");
    }

    @Test
    void testCanBeMerged() {
        System.out.println("Test de la possibilité de fusionner le dragon...");

        assertTrue(dragon.canBeMerged());
        System.out.println("Le dragon peut être fusionné.");
    }

    @Test
    void testGetWidthAndHeight() {
        System.out.println("Test de la largeur et de la hauteur...");

        assertEquals(50, dragon.getWidth());
        assertEquals(50, dragon.getHeight());

        System.out.println("Test de la largeur et de la hauteur réussi.");
    }

    @Test
    void testDefend() {
        System.out.println("Test de la défense...");

        int initialHealth = dragon.getHealth();

        dragon.defend(20);

        assertEquals(initialHealth - 10, dragon.getHealth());
        System.out.println("Santé après défense : " + dragon.getHealth());
    }

    @Test
    void testIsAlive() {
        System.out.println("Test si le dragon est vivant...");

        assertTrue(dragon.isAlive(), "Le dragon devrait être vivant initialement.");

        dragon.setHealth(0);
        assertFalse(dragon.isAlive(), "Le dragon ne devrait pas être vivant après que sa santé soit à 0.");

        System.out.println("Test de la condition de vie réussi.");
    }
}
