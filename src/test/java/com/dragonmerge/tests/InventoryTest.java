package com.dragonmerge.tests;

import com.dragonmerge.model.Inventory;
import com.dragonmerge.model.Calice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void testBuyEgg() {
        System.out.println("Tentative d'achat d'un œuf...");
        assertTrue(inventory.buyEgg());
        System.out.println("Oeuf acheté avec succès.");
        assertEquals(270, inventory.getCoins());
        assertEquals(1, inventory.getEggs());
    }

    @Test
    void testMergeEggs() {
        inventory.addCoins(1000);
        for (int i = 0; i < 3; i++) {
            inventory.buyEgg();
        }
        assertTrue(inventory.mergeEggs());
        assertEquals(0, inventory.getEggs());
        assertEquals(1, inventory.getDragonsByLevel(1));
        System.out.println("Oeufs fusionnés avec succès en un dragon de niveau 1.");
    }
}
