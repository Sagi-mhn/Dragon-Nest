package com.dragonmerge.tests;

import com.dragonmerge.model.Projectile;
import com.dragonmerge.utils.JavaFXInitializer;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {

    @BeforeAll
    public static void initToolkit() throws Exception {
        JavaFXInitializer.initialize();
    }

    public class ToolkitInitializedHelper {
        private static boolean initialized = false;

        public static synchronized boolean isInitialized() {
            return initialized;
        }

        public static synchronized void setInitialized() {
            initialized = true;
        }
    }

    @Test
    public void testInitialProperties() {
        Projectile projectile = new Projectile(100, 100, "UP", 5);

        assertEquals(100, projectile.getX(), "La position X initiale du projectile doit être correcte");
        assertEquals(100, projectile.getY(), "La position Y initiale du projectile doit être correcte");
        assertEquals("UP", projectile.getDirection(), "La direction initiale du projectile doit être correcte");
        assertEquals(5, projectile.getSpeed(), "La vitesse initiale du projectile doit être correcte");
        assertEquals(270, projectile.getRotate(), "L'orientation du projectile doit correspondre à la direction");
    }

    @Test
    public void testMovement() {
        Projectile projectile = new Projectile(100, 100, "RIGHT", 5);

        projectile.move(10, 15);
        assertEquals(110, projectile.getX(), "Le déplacement en X doit être correct");
        assertEquals(115, projectile.getY(), "Le déplacement en Y doit être correct");
    }

    @Test
    public void testOutOfBounds() {
        Projectile projectile = new Projectile(50, 50, "UP", 5);

        assertTrue(projectile.isOutOfBounds(), "Le projectile doit être hors des limites au départ");

        projectile = new Projectile(200, 200, "UP", 5);
        assertFalse(projectile.isOutOfBounds(), "Le projectile doit être dans les limites de l'arène");
    }

    @Test
    public void testDirectionRotation() {
        Projectile projectile = new Projectile(100, 100, "DOWN", 5);
        assertEquals(90, projectile.getRotate(), "La rotation doit correspondre à la direction DOWN");

        projectile = new Projectile(100, 100, "LEFT", 5);
        assertEquals(180, projectile.getRotate(), "La rotation doit correspondre à la direction LEFT");

        projectile = new Projectile(100, 100, "UP_RIGHT", 5);
        assertEquals(315, projectile.getRotate(), "La rotation doit correspondre à la direction UP_RIGHT");
    }
}
