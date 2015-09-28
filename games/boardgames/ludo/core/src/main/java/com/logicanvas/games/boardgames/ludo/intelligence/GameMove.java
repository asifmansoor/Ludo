package com.logicanvas.games.boardgames.ludo.intelligence;

import com.logicanvas.games.boardgames.ludo.model.PlayerToken;

/**
 * Created by amansoor on 24-08-2015.
 *
 * This class is to define a game move
 */
public class GameMove {
    public static final int IDLE            = 0;
    public static final int OPEN            = 1;
    public static final int MOVE            = 2;
    public static final int MOVE_AND_HIT    = 3;
    public static final int RESET           = 4;
    public static final int ENTER           = 5;
    public static final int HOME            = 6;

    private int playerId;
    private int tokenIndex;
    private int moveType;
    private int moveSteps;
    private int moveScore;
    private int finalLocation;
    private PlayerToken hitOpponent;

    public GameMove(int playerId, int tokenIndex, int moveType, int moveSteps, int moveScore, int finalLocation) {
        this.playerId = playerId;
        this.tokenIndex = tokenIndex;
        this.moveType = moveType;
        this.moveSteps = moveSteps;
        this.moveScore = moveScore;
        this.finalLocation = finalLocation;
    }

    public GameMove(int playerId, int tokenIndex, int moveType, int moveSteps, int moveScore, int finalLocation, PlayerToken hitOpponent) {
        this.playerId = playerId;
        this.tokenIndex = tokenIndex;
        this.moveType = moveType;
        this.moveSteps = moveSteps;
        this.moveScore = moveScore;
        this.finalLocation = finalLocation;
        this.hitOpponent = hitOpponent;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getTokenIndex() {
        return tokenIndex;
    }

    public int getMoveType() {
        return moveType;
    }

    public int getMoveSteps() {
        return moveSteps;
    }

    public int getMoveScore() {
        return moveScore;
    }

    public void setMoveScore(int moveScore) {
        this.moveScore = moveScore;
    }

    public int getFinalLocation() {
        return finalLocation;
    }

    public PlayerToken getHitOpponent() {
        return hitOpponent;
    }
}
