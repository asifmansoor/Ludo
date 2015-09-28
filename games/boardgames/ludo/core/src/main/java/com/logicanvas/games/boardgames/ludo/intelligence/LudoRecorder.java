package com.logicanvas.games.boardgames.ludo.intelligence;

import com.logicanvas.games.boardgames.ludo.config.GameConfiguration;
import com.logicanvas.games.boardgames.ludo.model.GameData;
import playn.core.Platform;

/**
 * Created by amansoor on 05-09-2015.
 */
public class LudoRecorder {

    private GameData gameData;
    private Platform platform;

    public LudoRecorder(GameData gameData, Platform platform) {
        this.gameData = gameData;
        this.platform = platform;
    }

    public boolean saveGame() {
        int playerId = gameData.getTurn();
        platform.storage().setItem("Round", String.valueOf(gameData.getRound()));
        platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"dice", String.valueOf(gameData.getDiceRoll()));
        platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"turn", String.valueOf(gameData.getTurn()));
        platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"playerBeaten", String.valueOf(gameData.getPlayer(playerId).isHasPlayerBeatenAToken()));
        platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"playerScore", String.valueOf(gameData.getPlayer(playerId).getPlayerScore()));
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"Token"+i+"index", String.valueOf(gameData.getPlayer(playerId).getPlayerToken(i).getIndex()));
            platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"Token"+i+"location", String.valueOf(gameData.getPlayer(playerId).getPlayerToken(i).getLocation()));
            platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"Token"+i+"state", String.valueOf(gameData.getPlayer(playerId).getPlayerToken(i).getState()));
            platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"Token"+i+"score", String.valueOf(gameData.getPlayer(playerId).getPlayerToken(i).getTokenScore()));
            platform.storage().setItem("Round"+gameData.getRound()+"Player"+playerId+"Token"+i+"playerid", String.valueOf(gameData.getPlayer(playerId).getPlayerToken(i).getPlayerId()));
        }
        platform.storage().startBatch();
        return platform.storage().isPersisted();
    }

    public void loadGame(int round, int playerId) {
        String latestRound = platform.storage().getItem("Round");
        if (round > Integer.parseInt(latestRound)) {
            return;
        }
        gameData.setRound(Integer.parseInt(platform.storage().getItem("Round" + round)));
        gameData.setDiceRoll(Integer.parseInt(platform.storage().getItem("Round" + round + "Player" + playerId + "dice")));
        gameData.setTurn(Integer.parseInt(platform.storage().getItem("Round" + round + "Player" + playerId + "turn")));
        gameData.getPlayer(playerId).setHasPlayerBeatenAToken(Boolean.parseBoolean(platform.storage().getItem("Round" + gameData.getRound() + "Player" + playerId + "playerBeaten")));
        gameData.getPlayer(playerId).setPlayerScore(Integer.parseInt(platform.storage().getItem("Round" + gameData.getRound() + "Player" + playerId + "playerScore")));
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            gameData.getPlayer(playerId).getPlayerToken(i).setIndex(Integer.parseInt(platform.storage().getItem("Round" + gameData.getRound() + "Player" + playerId + "Token" + i + "index")));
            gameData.getPlayer(playerId).getPlayerToken(i).setLocation(Integer.parseInt(platform.storage().getItem("Round" + gameData.getRound() + "Player" + playerId + "Token" + i + "location")));
            gameData.getPlayer(playerId).getPlayerToken(i).setState(Integer.parseInt(platform.storage().getItem("Round" + gameData.getRound() + "Player" + playerId + "Token" + i + "state")));
            gameData.getPlayer(playerId).getPlayerToken(i).setTokenScore(Integer.parseInt(platform.storage().getItem("Round" + gameData.getRound() + "Player" + playerId + "Token" + i + "score")));
            gameData.getPlayer(playerId).getPlayerToken(i).setPlayerId(Integer.parseInt(platform.storage().getItem("Round" + gameData.getRound() + "Player" + playerId + "Token" + i + "playerid")));
        }
    }

    public void loadLastGame() {
        int latestRound = Integer.parseInt(platform.storage().getItem("Round"));
        loadGame(latestRound, 0);
    }
}
