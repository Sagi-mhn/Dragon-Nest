package com.dragonmerge.tests;

import com.dragonmerge.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
    }

    @Test
    public void testSetupBattleLevel1() {
        Battle battle = new Battle(player, 1);
        assertEquals(1, battle.getOpponents().size(), "Le niveau 1 doit avoir 1 adversaire");
        Opponent opponent = battle.getOpponents().get(0);
        assertEquals(5.0, opponent.getSpeed(), "L'adversaire du niveau 1 doit avoir une vitesse de 5.0");
        assertEquals(100, opponent.getHealth(), "L'adversaire du niveau 1 doit avoir 100 PV");
    }

    @Test
    public void testSetupBattleLevel2() {
        Battle battle = new Battle(player, 2);
        assertEquals(1, battle.getOpponents().size(), "Le niveau 2 doit avoir 1 adversaire");
        Opponent opponent = battle.getOpponents().get(0);
        assertEquals(7.0, opponent.getSpeed(), "L'adversaire du niveau 2 doit avoir une vitesse de 7.0");
        assertEquals(100, opponent.getHealth(), "L'adversaire du niveau 2 doit avoir 100 PV");
    }

    @Test
    public void testSetupBattleLevel3() {
        Battle battle = new Battle(player, 3);
        assertEquals(2, battle.getOpponents().size(), "Le niveau 3 doit avoir 2 adversaires");
        Opponent opponent1 = battle.getOpponents().get(0);
        Opponent opponent2 = battle.getOpponents().get(1);
        assertEquals(5.0, opponent1.getSpeed(), "Les adversaires du niveau 3 doivent avoir une vitesse de 5.0");
        assertEquals(100, opponent1.getHealth(), "Les adversaires du niveau 3 doivent avoir 100 PV");
        assertEquals(7.0, player.getDragon().getSpeed(),
                "Le dragon du joueur doit avoir une vitesse de 7.0 au niveau 3");
    }

    @Test
    public void testSetupBattleLevel4() {
        Battle battle = new Battle(player, 4);
        assertEquals(2, battle.getOpponents().size(), "Le niveau 4 doit avoir 2 adversaires");
        Opponent opponent1 = battle.getOpponents().get(0);
        Opponent opponent2 = battle.getOpponents().get(1);
        assertEquals(5.0, opponent1.getSpeed(), "Les adversaires du niveau 4 doivent avoir une vitesse de 5.0");
        assertEquals(150, opponent1.getHealth(), "Les adversaires du niveau 4 doivent avoir 150 PV");
        assertEquals(150, opponent2.getHealth(), "Les adversaires du niveau 4 doivent avoir 150 PV");
    }

    @Test
    public void testSetupBattleLevel5() {
        Battle battle = new Battle(player, 5);
        assertEquals(1, battle.getOpponents().size(), "Le niveau 5 doit avoir 1 miniboss");
        Opponent miniboss = battle.getOpponents().get(0);
        assertTrue(miniboss instanceof Miniboss, "L'adversaire du niveau 5 doit être un miniboss");
        assertEquals(180, miniboss.getHealth(), "Le miniboss doit avoir 180 PV");
        assertEquals(5.0, miniboss.getSpeed(), "Le miniboss doit avoir une vitesse de 5.0");
    }

    @Test
    public void testBattleVictory() {
        Battle battle = new Battle(player, 1);
        player.getDragon().setHealth(100);
        Opponent opponent = battle.getOpponents().get(0);
        opponent.setHealth(10);

        boolean result = battle.start();
        assertTrue(result,
                "Le joueur doit gagner si son dragon est toujours en vie et tous les adversaires sont vaincus");
    }

    @Test
    public void testBattleDefeat() {
        Battle battle = new Battle(player, 1);
        player.getDragon().setHealth(10);
        Opponent opponent = battle.getOpponents().get(0);
        opponent.setHealth(100);

        boolean result = battle.start();
        assertFalse(result, "Le joueur doit perdre si son dragon est vaincu");
    }
}
