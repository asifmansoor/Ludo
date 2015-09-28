package com.logicanvas.frameworks.boardgamesgdk.core.gameplay;

import com.logicanvas.frameworks.boardgamesgdk.core.config.BasicGameConfiguration;

/**
 * Created by amansoor on 28-09-2015.
 */
public class BasicPlayer {
    public static final int PLAYER_TYPE_OFF = 0;
    public static final int PLAYER_TYPE_AI_LEVEL1 = 1;
    public static final int PLAYER_TYPE_HUMAN = 2;

    protected int playerId;                                 // id of the player
    protected int playerType;

    public BasicPlayer(int playerId) {
        this.playerId = playerId;
        this.playerType = PLAYER_TYPE_OFF;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }
}
