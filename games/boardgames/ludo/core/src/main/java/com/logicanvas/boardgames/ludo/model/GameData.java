package com.logicanvas.boardgames.ludo.model;

import com.logicanvas.boardgames.ludo.config.GameConfiguration;
import com.logicanvas.boardgames.ludo.core.LudoPlayer;
import com.logicanvas.boardgames.ludo.intelligence.MoveSequence;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.BoardGamesLogger;

import java.util.ArrayList;

/**
 * Created by amansoor on 24-08-2015.
 */
public class GameData {
    private LudoPlayer[] players;
    private MoveSequence moveSequence;
    private int turn;
    private int diceRoll;
    private GameConfiguration.GAME_STATE gameState;
    private int playerMove;
    private int round;
    private ArrayList<Integer> playerMoveOptions;


    public LudoPlayer[] getPlayers() {
        return players;
    }

    public void setPlayers(LudoPlayer[] players) {
        this.players = players;
    }

    public LudoPlayer getPlayer(int playerId) {
        if (players != null) {
            return players[playerId];
        } else {
            BoardGamesLogger.debug("Player data not set");
        }

        return null;
    }

    public void setMoveSequence(MoveSequence moveSequence) {
        this.moveSequence = moveSequence;
    }

    public MoveSequence getMoveSequence() {
        return moveSequence;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getDiceRoll() {
        return diceRoll;
    }

    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    public GameConfiguration.GAME_STATE getGameState() {
        return gameState;
    }

    public void setGameState(GameConfiguration.GAME_STATE gameState) {
        this.gameState = gameState;
    }

    public int getPlayerMove() {
        return playerMove;
    }

    public void setPlayerMove(int playerMove) {
        this.playerMove = playerMove;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public ArrayList<Integer> getPlayerMoveOptions() {
        return playerMoveOptions;
    }

    public void setPlayerMoveOptions(ArrayList<Integer> playerMoveOptions) {
        this.playerMoveOptions = playerMoveOptions;
    }
}
