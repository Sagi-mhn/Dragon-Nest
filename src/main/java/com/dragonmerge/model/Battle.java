package com.dragonmerge.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a battle in the game between the player's dragon and a set of
 * opponents.
 * Each battle is associated with a specific level and has predefined opponents
 * and conditions.
 */
public class Battle {

    private Player player;
    private List<Opponent> opponents;

    /**
     * Constructs a Battle instance for a specific level.
     *
     * @param player      The player participating in the battle.
     * @param levelNumber The level number determining the setup of the battle.
     */
    public Battle(Player player, int levelNumber) {
        this.player = player;
        this.opponents = new ArrayList<>();
        setupBattle(levelNumber);
    }

    /**
     * Sets up the battle by adding opponents based on the level number.
     *
     * @param levelNumber The level number to configure the battle's opponents and
     *                    conditions.
     */
    private void setupBattle(int levelNumber) {
        switch (levelNumber) {
            case 1:
                Opponent opponent1 = new Opponent(5.0, 100);
                opponents.add(opponent1);
                System.out.println("Niveau 1 : 1 adversaire, vitesse 5");
                break;
            case 2:
                Opponent opponent2 = new Opponent(7.0, 100);
                opponents.add(opponent2);
                System.out.println("Niveau 2 : 1 adversaire, vitesse 7");
                break;
            case 3:
                player.getDragon().setSpeed(7.0);
                Opponent opp1 = new Opponent(5.0, 100);
                Opponent opp2 = new Opponent(5.0, 100);
                opponents.add(opp1);
                opponents.add(opp2);
                System.out.println("Niveau 3 : 2 adversaires, vitesse 5, dragon vitesse 7");
                break;
            case 4:
                Opponent opp3 = new Opponent(5.0, 150);
                Opponent opp4 = new Opponent(5.0, 150);
                opponents.add(opp3);
                opponents.add(opp4);
                System.out.println("Niveau 4 : 2 dragons adversaires avec 150 PV, vitesse 5");
                break;
            case 5:
                player.getDragon().setSpeed(7.0);
                Miniboss miniboss = new Miniboss(5.0, 180, 2);
                opponents.add(miniboss);
                System.out.println(
                        "Niveau 5 : Mini-boss avec 180 PV, tire 2 projectiles en meme temps, dragon vitesse 7");
                break;
            default:
                Opponent defaultOpponent = new Opponent(5.0, 100);
                opponents.add(defaultOpponent);
                System.out.println("Niveau par défaut : 1 adversaire, vitesse 5");
                break;
        }
    }

    public boolean start() {
        return runBattleRound();
    }

    private boolean runBattleRound() {
        while (player.getDragon().getHealth() > 0 && !opponents.isEmpty()) {
            for (Opponent opponent : opponents) {
                player.attack(opponent);
                if (opponent.isAlive()) {
                    opponent.attack(player.getDragon());
                } else {
                    System.out.println("Vous avez vaincu un adversaire !");
                }
            }
            opponents.removeIf(opponent -> !opponent.isAlive());

            if (opponents.isEmpty()) {
                System.out.println("Vous avez gagné la bataille !");
                return true;
            }

            if (!player.getDragon().isAlive()) {
                System.out.println("Vous avez perdu la bataille.");
                return false;
            }
        }
        return false;
    }

    public List<Opponent> getOpponents() {
        return opponents;
    }
}
