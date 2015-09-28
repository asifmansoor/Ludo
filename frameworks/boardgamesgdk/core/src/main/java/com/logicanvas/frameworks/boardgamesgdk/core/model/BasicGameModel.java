package com.logicanvas.frameworks.boardgamesgdk.core.model;

import com.logicanvas.frameworks.boardgamesgdk.core.gameplay.BasicPlayer;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.BoardGamesLogger;

/**
 * Created by amansoor on 28-09-2015.
 */
public class BasicGameModel {

    private BasicPlayer[] players;

    public BasicPlayer[] getPlayers() {
        return players;
    }

    public BasicPlayer getPlayers(int playerId) {
        if (players != null && playerId < players.length) {
            return players[playerId];
        } else {
            BoardGamesLogger.error("Incorrect player Id");
            return null;
        }
    }

    public void setPlayers(BasicPlayer[] players) {
        this.players = players;
    }
}
