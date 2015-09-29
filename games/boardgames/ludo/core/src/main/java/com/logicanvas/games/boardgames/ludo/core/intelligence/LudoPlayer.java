package com.logicanvas.games.boardgames.ludo.core.intelligence;


import com.logicanvas.frameworks.boardgamesgdk.core.gameplay.BasicPlayer;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.BoardGamesLogger;
import com.logicanvas.games.boardgames.ludo.core.config.GameConfiguration;
import com.logicanvas.games.boardgames.ludo.core.model.PlayerToken;

/**
 * Created by amansoor on 24-08-2015.
 * <p/>
 * This class is to track the various properties of a Ludo Player
 */
public class LudoPlayer extends BasicPlayer {

    public final int startLocationIndex;                  // index of the board start location for the tokens
    public final int openLocationIndex;                   // index of the open location on board
    private int homeRowStartIndex;                        // index of the home row starting position
    private PlayerToken[] playerTokens;                   // token objects for the player
    private boolean hasPlayerBeatenAToken = false;        /* flag for check whether player has beaten another player
                                                             or not
                                                           */
    private int playerScore;                              // total score of the player
    private int rank;                                     // rank of the player

    public LudoPlayer(int id, int startLoc, int openLoc, int homeRowStartLoc) {
        super(id);
        startLocationIndex = startLoc;
        openLocationIndex = openLoc;
        homeRowStartIndex = homeRowStartLoc;
        playerScore = 0;

        playerTokens = new PlayerToken[GameConfiguration.NO_OF_TOKENS_PER_PLAYER];
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            playerTokens[i] = new PlayerToken();
            playerTokens[i].setState(GameConfiguration.TOKEN_STATE_UNOPEN);
            playerTokens[i].setTokenScore(0);
            playerTokens[i].setLocation(startLocationIndex + i);
            playerTokens[i].setIndex(i);
            playerTokens[i].setPlayerId(playerId);
        }
   }

    public void resetPlayer() {
        playerScore = 0;
        hasPlayerBeatenAToken = false;
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            playerTokens[i].setState(GameConfiguration.TOKEN_STATE_UNOPEN);
            playerTokens[i].setTokenScore(0);
            playerTokens[i].setLocation(startLocationIndex + i);
            playerTokens[i].setIndex(i);
            playerTokens[i].setPlayerId(playerId);
        }
    }

    public boolean isAllHomeForPlayer() {
        if (getNumPlayerTokensInState(GameConfiguration.TOKEN_STATE_HOME) == GameConfiguration.NO_OF_TOKENS_PER_PLAYER) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumPlayerTokensInState(int state) {
        int count = 0;
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            if (playerTokens[i].getState() == state) {
                count++;
            }
        }
        return count;
    }

    public int getNumPlayerTokensUnOpened() {
        return getNumPlayerTokensInState(GameConfiguration.TOKEN_STATE_UNOPEN);
    }

    public void playMove(GameMove gameMove) {
        switch (gameMove.getMoveType()) {
            case GameMove.OPEN:
                BoardGamesLogger.debug("move: OPEN : playerid: " + playerId + " tokenIndex: " + gameMove.getTokenIndex());
                openToken(gameMove.getTokenIndex());
                break;
            case GameMove.MOVE:
                BoardGamesLogger.debug("move: MOVE : playerid: "+playerId+" tokenIndex: "+gameMove.getTokenIndex());
                moveToken(gameMove.getTokenIndex(), gameMove.getMoveSteps(), gameMove.getFinalLocation());
                break;
            case GameMove.MOVE_AND_HIT:
                BoardGamesLogger.debug("move: MOVE AND HIT : playerid: "+playerId+" tokenIndex: "+gameMove.getTokenIndex());
                moveAndHitToken(gameMove.getTokenIndex(), gameMove.getMoveSteps(), gameMove.getFinalLocation());
                break;
            case GameMove.RESET:
                BoardGamesLogger.debug("move: RESET : playerid: "+playerId+" tokenIndex: "+gameMove.getTokenIndex());
                resetToken(gameMove.getTokenIndex());
                break;
            case GameMove.ENTER:
                BoardGamesLogger.debug("move: ENTER : playerid: "+playerId+" tokenIndex: "+gameMove.getTokenIndex());
                enterLastRow(gameMove.getTokenIndex(), gameMove.getMoveSteps());
                break;
            case GameMove.HOME:
                BoardGamesLogger.debug("move: HOME : playerid: "+playerId+" tokenIndex: "+gameMove.getTokenIndex());
                enterTokenHome(gameMove.getTokenIndex(), gameMove.getMoveSteps());
                break;
            case GameMove.IDLE:
                BoardGamesLogger.debug("move: IDLE : playerid: "+playerId+" tokenIndex: "+gameMove.getTokenIndex());
                break;
        }
    }

    public void resetToken(int tokenIndex) {
        playerTokens[tokenIndex].setLocation(startLocationIndex + tokenIndex);
        playerTokens[tokenIndex].setTokenScore(0);
        playerTokens[tokenIndex].setState(GameConfiguration.TOKEN_STATE_UNOPEN);
    }

    public void openToken(int tokenIndex) {
        playerTokens[tokenIndex].setLocation(openLocationIndex);
        playerTokens[tokenIndex].setTokenScore(GameConfiguration.POINTS_OPEN_TOKEN);
        playerTokens[tokenIndex].setState(GameConfiguration.TOKEN_STATE_OPEN);
    }

    public void enterLastRow(int tokenIndex, int numMoves) {
        playerTokens[tokenIndex].setLocation(homeRowStartIndex + numMoves);
        playerTokens[tokenIndex].updateTokenScore(numMoves + 100);
        playerTokens[tokenIndex].setState(GameConfiguration.TOKEN_STATE_IN_HOME_ROW);
    }

    private void enterTokenHome(int tokenIndex, int numMoves) {
        playerTokens[tokenIndex].setLocation(homeRowStartIndex + 5);
        playerTokens[tokenIndex].setTokenScore(numMoves+200);
        playerTokens[tokenIndex].setState(GameConfiguration.TOKEN_STATE_HOME);
    }

    public void moveToken(int tokenIndex, int numMoves, int finalLocation) {
        playerTokens[tokenIndex].setLocation(finalLocation);
        playerTokens[tokenIndex].updateTokenScore(numMoves);
    }

    public void moveAndHitToken(int tokenIndex, int numMoves, int finalLocation) {
        moveToken(tokenIndex, numMoves, finalLocation);
        setHasPlayerBeatenAToken(true);
        playerTokens[tokenIndex].updateTokenScore(50);
    }

    public boolean tokenHasValidMove(int tokenId, int numMoves) {
        int playedMoves = playerTokens[tokenId].getLocation();
        if (playedMoves > GameConfiguration.GAME_CYCLE_END_INDEX) {
            int homeRowMoves = playedMoves - homeRowStartIndex + 1;
            playedMoves = homeRowMoves + GameConfiguration.GAME_CYCLE_END_INDEX;
        }

        if (playedMoves + numMoves <= GameConfiguration.MAX_TOKEN_MOVE)
            return true;
        else
            return false;
    }

    public int getStartLocationIndex() {
        return startLocationIndex;
    }

    public int getHomeRowStartIndex() {
        return homeRowStartIndex;
    }

    public boolean isHasPlayerBeatenAToken() {
        return hasPlayerBeatenAToken;
    }

    public PlayerToken getPlayerToken(int tokenIndex) {
        return playerTokens[tokenIndex];
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setHasPlayerBeatenAToken(boolean hasPlayerBeatenAToken) {
        this.hasPlayerBeatenAToken = hasPlayerBeatenAToken;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }
}
