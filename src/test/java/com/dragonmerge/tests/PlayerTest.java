package com.dragonmerge.tests;

import com.dragonmerge.utils.*;
import com.dragonmerge.model.*;
import com.dragonmerge.view.DragonView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeAll
    public static void initToolkit() throws Exception {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    public void setUp() {
        player = new Player();
    }

    @Test
    public void testInitialPlayerSetup() {
        assertNotNull(player.getDragon(), "Le dragon ne doit pas être null au démarrage");
        assertNotNull(player.getDragonView(), "La vue du dragon ne doit pas être null au démarrage");
        assertNotNull(player.getInventory(), "L'inventaire du joueur ne doit pas être null");
        assertEquals(5.0, player.getDragon().getSpeed(), "La vitesse du dragon doit être de 5.0");
        assertEquals(Direction.RIGHT, player.getDragon().getDirection(),
                "Le dragon doit initialement se diriger à droite");
    }

    @Test
    public void testBuyEgg() {
        boolean result = player.buyEgg();
        assertTrue(result, "L'achat d'un œuf devrait réussir si les conditions sont remplies");
    }

    @Test
    public void testMergeEggsToDragon() {
        boolean result = player.mergeEggsToDragon();
        assertFalse(result, "La fusion d'œufs devrait échouer si aucun œuf n'est disponible");
    }

    @Test
    public void testMergeDragons() {
        boolean result = player.mergeDragons(1);
        assertFalse(result, "La fusion de dragons devrait échouer si aucun dragon n'est disponible");
    }

    @Test
    public void testDefend() {
        int initialHealth = player.getDragon().getHealth();
        player.defend();
        assertEquals(initialHealth - 5, player.getDragon().getHealth(),
                "La défense devrait réduire la santé du dragon de 5 points");
    }

    @Test
    public void testResetDragon() {
        player.resetDragon();
        assertNotNull(player.getDragon(), "Le dragon ne doit pas être null après un reset");
        assertEquals(Direction.RIGHT, player.getDragon().getDirection(),
                "Après un reset, le dragon doit être orienté à droite");
    }
}
