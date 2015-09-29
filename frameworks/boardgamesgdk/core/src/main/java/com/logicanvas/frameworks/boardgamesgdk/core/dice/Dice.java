package com.logicanvas.frameworks.boardgamesgdk.core.dice;


import com.logicanvas.frameworks.boardgamesgdk.core.config.BasicGameConfiguration;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.BoardGamesLogger;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by amansoor on 24-08-2015.
 *
 * This class is to get dice outcomes
 */
public class Dice {
    private int[] dice = {-1, -1};
    private Random rng = new Random();

    private ArrayList diceRig = new ArrayList();

    public Dice() {
        if (BasicGameConfiguration.RIG_GAME) {
            for (int i = 0; i < BasicGameConfiguration.rigList.length; i++) {
                diceRig.add(BasicGameConfiguration.rigList[i]);
            }
        }
    }

    public int[] twoDiceOutcome() {
        dice[0] = rng.nextInt(6);
        dice[1] = rng.nextInt(6);
        return dice;
    }

    public int oneDiceOutcome() {
        if (diceRig != null && !diceRig.isEmpty() && BasicGameConfiguration.RIG_GAME)
            dice[0] = (int) diceRig.remove(0);
        else
            dice[0] = rng.nextInt(6) + 1;
        BoardGamesLogger.debug("Dice :" + dice[0]);
        return dice[0];
    }
}
