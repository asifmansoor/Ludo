package com.logicanvas.games.boardgames.ludo.intelligence;

import com.logicanvas.frameworks.boardgamesgdk.core.utility.BoardGamesLogger;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.Utility;
import com.logicanvas.games.boardgames.ludo.config.GameConfiguration;
import com.logicanvas.games.boardgames.ludo.config.GameRules;
import com.logicanvas.games.boardgames.ludo.model.GameData;
import com.logicanvas.games.boardgames.ludo.model.PlayerToken;

import java.util.ArrayList;

/**
 * Created by Asif Mansoor on 24-08-2015.
 *
 */

//testing
public class MoveEvaluator {
    private GameData gameData;

    public MoveEvaluator(GameData gameData) {
        this.gameData = gameData;
    }

    public GameMove evaluateMove() {
        ArrayList moves = new ArrayList(GameConfiguration.NO_OF_TOKENS_PER_PLAYER);
        GameMove move = null;
        LudoPlayer player = gameData.getPlayer(gameData.getTurn());
        int highestScore = GameConfiguration.POINTS_IDLE_TOKEN;
        int maxMoveScoreTokenIndex = -1;    // Variable to track the index of the token with the maximum move score

        calculatePlayerRanks();

        ArrayList<Integer> moveOpts = new ArrayList<>();

        // calculate the score for each token move and select the move which maximizes the player score
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            move = calculateMove(i);
            if (move != null) {
                if (move.getMoveType() != GameMove.IDLE) {
                    moveOpts.add(i);
                }
                moves.add(move);
                if (move.getMoveScore() >= highestScore) {
                    maxMoveScoreTokenIndex = i;
                    highestScore = move.getMoveScore();
                }
            }
        }
        gameData.setPlayerMoveOptions(moveOpts);

        if (!moves.isEmpty()) {
            move = (GameMove) moves.get(maxMoveScoreTokenIndex);
        }
        return move;
    }

    public GameMove evaluateMove(int tokenIndex) {
        GameMove move = calculateMove(tokenIndex);
        return move;
    }

    /*
     * Method to set ranks of the players
     */
    private void calculatePlayerRanks() {
        for (int index = 0; index < GameConfiguration.NO_OF_PLAYERS; index++) {
            gameData.getPlayer(index).setRank(getPlayerRank(gameData.getPlayer(index)));
        }
    }

    /*
     * Method to get the rank of a player
     */
    private int getPlayerRank(LudoPlayer player) {
        int rank = GameConfiguration.NO_OF_PLAYERS;
        for (int index = 0; index < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; index++) {
            if (player.getPlayerId() != gameData.getPlayer(index).getPlayerId()
                    && gameData.getPlayer(index).getPlayerScore() <= player.getPlayerScore()) {
                rank--;
            }
        }

        return rank;
    }

    //private boolean canTokenBeOpened

    private GameMove calculateMove(int tokenIndex) {
        GameMove gameMove = null;
        int playerId = gameData.getTurn();
        int diceRoll = gameData.getDiceRoll();
        int tokenLocation = gameData.getPlayer(playerId).getPlayerToken(tokenIndex).getLocation();
        int moveScore = 0;
        int opportunityScore = 0;
        int threatPerceptionScore = 0;
        int finalLocation = -1;
        PlayerToken opponentHit = null;

        // check if the token is closed and can be opened
        if (gameData.getPlayer(playerId).getPlayerToken(tokenIndex).getState() == GameConfiguration.TOKEN_STATE_UNOPEN && Utility.findInArray(GameRules
                .tokenOpeningMoves, diceRoll) != -1) {
            // can open token
            // TODO: can check for possibility of beating or getting beaten
            moveScore = GameConfiguration.POINTS_CAN_OPEN_TOKEN;
            finalLocation = gameData.getPlayer(playerId).getStartLocationIndex();
            gameMove = new GameMove(playerId, tokenIndex, GameMove.OPEN, 0, moveScore, finalLocation);
            BoardGamesLogger.debug("evaluate: playerid : " + playerId + " tokenIndex: " + tokenIndex + " moveScore: " + moveScore + " " +
                    "outcome: " +
                    "OPEN");
            return gameMove;
        }

        // check if token can enter home
        if ((gameData.getPlayer(playerId).getPlayerToken(tokenIndex).getState() == GameConfiguration.TOKEN_STATE_IN_HOME_ROW && diceRoll == (gameData.getPlayer(playerId).getHomeRowStartIndex() + 5
                - tokenLocation)) || (tokenLocation == GameConfiguration.HOME_ROW_CHECK_INDEX_LIST[playerId] && diceRoll == 6)) {
            // token enters home
            moveScore = GameConfiguration.POINTS_TOKEN_HOME;
            finalLocation = gameData.getPlayer(playerId).getHomeRowStartIndex() + 5;
            gameMove = new GameMove(playerId, tokenIndex, GameMove.HOME, diceRoll, moveScore, finalLocation);
            BoardGamesLogger.debug("evaluate: playerid : " + playerId + " tokenIndex: " + tokenIndex + " moveScore: " + moveScore + " " +
                    "outcome: " +
                    "HOME");
            return gameMove;
        }

        // check for invalid moves
        if (!isValidMove(tokenIndex)) {
            moveScore = GameConfiguration.POINTS_IDLE_TOKEN;
            finalLocation = tokenLocation;
            gameMove = new GameMove(playerId, tokenIndex, GameMove.IDLE, 0, moveScore, finalLocation);
            BoardGamesLogger.debug("evaluate: playerid : " + playerId + " tokenIndex: " + tokenIndex + " moveScore: " + moveScore + " " +
                    "outcome: IDLE");
            return gameMove;
        } else {
            // check quadrant
            int quad = getPlayerTokenQuadrant(tokenIndex);
            finalLocation = getUpdatedLocation(playerId, tokenIndex, diceRoll);

            BoardGamesLogger.debug("situation: playerid : " + playerId + " tokenIndex: " + tokenIndex);

            // calculate scores
            // If rule is off player can enter.If rule is on player can only enter if it has already beaten another
            if (finalLocation >= gameData.getPlayer(playerId).getHomeRowStartIndex()) {
                // can enter home row
                moveScore = GameConfiguration.POINTS_CAN_ENTER_HOME_ROW;
                BoardGamesLogger.debug("evaluate: playerid : " + playerId + " tokenIndex: " + tokenIndex + " moveScore: " + moveScore + " " +
                        "outcome: ENTER HOME ROW");
                gameMove = new GameMove(playerId, tokenIndex, GameMove.ENTER, finalLocation - gameData.getPlayer(playerId).getHomeRowStartIndex(), moveScore, finalLocation);
                return gameMove;
            } else {

                // TODO: check for multiple token proximity
                // check other opponent proximity
                opponentHit = checkOpponentHit(finalLocation);
                PlayerToken opponentBehindBeforeMove = checkPlayerProximity(tokenLocation, GameConfiguration.REVERSE);
                PlayerToken opponentBehindAfterMove = checkPlayerProximity(finalLocation, GameConfiguration.REVERSE);
                PlayerToken opponentAheadBeforeMove = checkPlayerProximity(tokenLocation, GameConfiguration.FORWARD);
                PlayerToken opponentAheadAfterMove = checkPlayerProximity(finalLocation, GameConfiguration.FORWARD);
                boolean tokenAlreadyOnSafeSpot = GameRules.safeSpotEnabled && Utility.findInArray(GameConfiguration.SAFE_SPOT_LIST, tokenLocation) != -1;
                boolean tokenCanLandOnSafeSpot = GameRules.safeSpotEnabled && Utility.findInArray(GameConfiguration.SAFE_SPOT_LIST, finalLocation) != -1;

                // check for safe spots and double up
                if (tokenCanLandOnSafeSpot) {
                    // can land on safe spot
                    opportunityScore += GameConfiguration.POINTS_CAN_LAND_ON_SAFE_SPOT * quad;
                    BoardGamesLogger.debug(" opportunityScore: " + opportunityScore + " probable outcome: SAFE SPOT");
                } else if (GameRules.doubleUpAdvantageEnabled && checkDoubleUp(tokenIndex, finalLocation)) {
                    // can move to a double up position
                    opportunityScore += GameConfiguration.POINTS_CAN_DOUBLE_UP * quad;
                    BoardGamesLogger.debug(" opportunityScore: "+ opportunityScore + " probable outcome: DOUBLEUP");
                }

                if (opponentBehindBeforeMove == null && opponentBehindAfterMove != null && !tokenCanLandOnSafeSpot) {
                    // Threat : If opponent behind after move and token not landing on safe spot
                    threatPerceptionScore += GameConfiguration.POINTS_OPPONENT_BEHIND_BEFORE_AND_AFTER_MOVE * quad;
                    BoardGamesLogger.debug(" threatPerceptionScore: " + threatPerceptionScore + " probable outcome: If opponent behind after move and token not landing on safe spot");
                }

                if (opponentBehindAfterMove != null && tokenAlreadyOnSafeSpot) {
                    // Threat : If opponent behind after move and token already on safe spot
                    threatPerceptionScore += GameConfiguration.POINTS_PLAYER_ALREADY_SAFE_SPOT *quad;
                    BoardGamesLogger.debug(" threatPerceptionScore: " + threatPerceptionScore + " probable outcome: If opponent behind after move and token already on safe spot");
                }

                if (opponentBehindBeforeMove != null && opponentBehindAfterMove == null) {
                    // Opportunity : If opponent behind before move and no opponent behind after move
                    opportunityScore += GameConfiguration.POINTS_OPPONENT_BEHIND_BEFORE_MOVE_AND_NOT_AFTER_MOVE * quad;
                    BoardGamesLogger.debug(" opportunityScore: " + opportunityScore + " probable outcome: If opponent behind before move and no opponent behind after move");
                }

                if (opponentAheadBeforeMove == null && opponentAheadAfterMove != null) {
                    // Opportunity : If no opponent ahead before move and ahead after move
                    opportunityScore += GameConfiguration.POINTS_NO_OPPONENT_AHEAD_BEFORE_AND_OPPONENT_AHEAD_AFTER_MOVE * quad;
                    BoardGamesLogger.debug(" opportunityScore: " + opportunityScore + " probable outcome: If no opponent ahead before move and ahead after move");
                }

                if (opponentAheadBeforeMove != null && opponentAheadAfterMove != null) {
                    // Opportunity : If opponent ahead before move and ahead after move
                    opportunityScore += GameConfiguration.POINTS_OPPONENT_AHEAD_BEFORE_AND_AFTER_MOVE * quad;
                    BoardGamesLogger.debug(" opportunityScore: " + opportunityScore + " probable outcome: If opponent ahead before move and ahead after move");
                }



                if (opponentHit != null) {
                    // Opportunity : If opponent getting hit after move
                    opportunityScore += GameConfiguration.POINTS_OPPONENT_AHEAD_BEFORE_MOVE_AND_HIT_AFTER_MOVE * quad;
                    BoardGamesLogger.debug(" opportunityScore: " + opportunityScore + " probable outcome: If opponent getting hit after move");
                }

            }

            // TODO: can check if player near an opponent opening path for threat


            // calculate total score by subtracting threat perception score from opportunity score
            moveScore = opportunityScore - threatPerceptionScore;
            if (moveScore == 0) {
                moveScore = quad;
            }

            if (opponentHit != null) {
                // setup move
                BoardGamesLogger.debug("evaluate: playerid : " + playerId + " tokenIndex: " + tokenIndex + " moveScore: " + moveScore + " " +
                        "outcome: move and hit");
                gameMove = new GameMove(playerId, tokenIndex, GameMove.MOVE_AND_HIT, diceRoll, moveScore, finalLocation, opponentHit);
            } else {

                // setup move
                BoardGamesLogger.debug("evaluate: playerid : " + playerId + " tokenIndex: " + tokenIndex + " moveScore: " + moveScore + " " +
                        "outcome: normal move");
                gameMove = new GameMove(playerId, tokenIndex, GameMove.MOVE, diceRoll, moveScore, finalLocation);
            }
        }

        return gameMove;
    }

    public boolean isThereAnyValidMoveForPlayer() {
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            if (isValidMove(i)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidMove(int tokenIndex) {
        if (gameData.getPlayer(gameData.getTurn()).getPlayerToken(tokenIndex).getState() == GameConfiguration.TOKEN_STATE_HOME) {
            // token is home
            BoardGamesLogger.debug("situation: playerid : " + gameData.getTurn() + " tokenIndex: " + tokenIndex + " outcome: token is already home");
            return false;
        } else if ((gameData.getPlayer(gameData.getTurn()).getPlayerToken(tokenIndex).getState() == GameConfiguration.TOKEN_STATE_UNOPEN) && Utility
                .findInArray(GameRules
                        .tokenOpeningMoves, gameData.getDiceRoll()) == -1) {
            // token is unopen and we don't have an opening move
            BoardGamesLogger.debug("situation: playerid : " + gameData.getTurn() + " tokenIndex: " + tokenIndex + " outcome: token is unopen and we don't have an opening move");
            return false;
        } else if ((gameData.getPlayer(gameData.getTurn()).getPlayerToken(tokenIndex).getState() != GameConfiguration.TOKEN_STATE_UNOPEN) && !gameData.getPlayer(gameData.getTurn()).tokenHasValidMove(tokenIndex, gameData.getDiceRoll())) {
            // token has no valid move
            BoardGamesLogger.debug("situation: playerid : " + gameData.getTurn() + " tokenIndex: " + tokenIndex + " outcome: token has no valid move");
            return false;
        } else {
            return true;
        }
    }

    /*
     * Method to check which quadrant is the player in. The quad index will change depending on the player since the
     * board positions are fixed hence we use the table in GameConfiguration to find the correct index
     */
    private int getPlayerTokenQuadrant(int tokenIndex) {
        int boardStartLocation = GameConfiguration.BLUE_OPEN_INDEX;
        int playerId = gameData.getTurn();
        int tokenLocation = gameData.getPlayer(playerId).getPlayerToken(tokenIndex).getLocation();
        if (tokenLocation >= boardStartLocation && tokenLocation <= boardStartLocation + 12) {
            return GameConfiguration.quadIndex[playerId][0];
        } else if (tokenLocation >= boardStartLocation + 13 && tokenLocation <= boardStartLocation + 25) {
            return GameConfiguration.quadIndex[playerId][1];
        } else if (tokenLocation >= boardStartLocation + 26 && tokenLocation <= boardStartLocation + 38) {
            return GameConfiguration.quadIndex[playerId][2];
        } else if ((tokenLocation >= boardStartLocation + 39 && tokenLocation <= boardStartLocation + 49) ||
                tokenLocation == 0 || tokenLocation == 1) {
            return GameConfiguration.quadIndex[playerId][3];
        }

        BoardGamesLogger.error("Unable to get the correct quadrant");
        return 0;
    }

    private boolean checkDoubleUp(int tokenIndex, int proposedLocation) {
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            if (i != tokenIndex) {
                if (gameData.getPlayer(gameData.getTurn()).getPlayerToken(i).getLocation() == proposedLocation) {
                    return true;
                }
            }
        }

        return false;
    }

    /*
     * checks if token can hit another player and returns the highest ranking player token being hit
     */
    private PlayerToken checkOpponentHit(int finalLocation) {
        ArrayList tokens = new ArrayList();

        if (Utility.findInArray(GameConfiguration.SAFE_SPOT_LIST, finalLocation) != -1) {
            // return null in case player landing on a safe spot as it cannot hit an opponent on a safe spot
            return null;
        }

        // Find all tokens being hit
        for (int playerIdCounter = 0; playerIdCounter < GameConfiguration.NO_OF_PLAYERS; playerIdCounter++) {
            if (gameData.getTurn() != playerIdCounter) {
                for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
                    if (finalLocation == gameData.getPlayer(playerIdCounter).getPlayerToken(i).getLocation()) {
                        tokens.add(gameData.getPlayer(playerIdCounter).getPlayerToken(i));
                    }
                }
            }
        }
        return getHighestRankedPlayerTokenFromList(tokens);
    }

    /*
     * Checks if there is another player behind
     */
    private PlayerToken checkPlayerProximity(int location, boolean direction) {
        ArrayList tokens = new ArrayList();

        // Find all tokens in proximity
        // TODO: check if players are on the same safe spot
        for (int playerIdCounter = 0; playerIdCounter < GameConfiguration.NO_OF_PLAYERS; playerIdCounter++) {
            if (gameData.getTurn() != playerIdCounter) {
                for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
                    if (direction) {
                        // ahead
                        if (isPlayerInRangeInForwardDirection(location, gameData.getPlayer(playerIdCounter).getPlayerToken(i)
                                .getLocation())) {
                            tokens.add(gameData.getPlayer(playerIdCounter).getPlayerToken(i));
                        }
                    } else {
                        //behind
                        if (isPlayerInRangeInBackwardDirection(location, gameData.getPlayer(playerIdCounter).getPlayerToken(i)
                                .getLocation())) {
                            tokens.add(gameData.getPlayer(playerIdCounter).getPlayerToken(i));
                        }
                    }
                }
            }
        }

        return getHighestRankedPlayerTokenFromList(tokens);
    }

    private PlayerToken getHighestRankedPlayerTokenFromList(ArrayList tokens) {
        if (!tokens.isEmpty()) {

            // Find the token with highest player value
            PlayerToken tokenInFocus = (PlayerToken) tokens.get(0);
            LudoPlayer playerInFocus = gameData.getPlayer(tokenInFocus.getPlayerId());
            int highestPlayerScoreInList = playerInFocus.getPlayerScore();
            int highestTokenScoreForTheSelectedPlayerInList = tokenInFocus.getTokenScore();
            for (int index = 1; index < tokens.size(); index++) {
                tokenInFocus = (PlayerToken) tokens.get(index);
                playerInFocus = gameData.getPlayer(tokenInFocus.getPlayerId());
                if (highestPlayerScoreInList == playerInFocus.getPlayerScore()) {
                    if (highestTokenScoreForTheSelectedPlayerInList < tokenInFocus.getTokenScore()) {
                        highestPlayerScoreInList = playerInFocus.getPlayerScore();
                        highestTokenScoreForTheSelectedPlayerInList = tokenInFocus.getTokenScore();
                    }
                } else if (highestPlayerScoreInList < playerInFocus.getPlayerScore()) {
                    highestPlayerScoreInList = playerInFocus.getPlayerScore();
                    highestTokenScoreForTheSelectedPlayerInList = tokenInFocus.getTokenScore();
                }
            }
            return tokenInFocus;
        }
        return null;
    }

    private boolean isPlayerInRangeInBackwardDirection(int location1, int location2) {
        if (location1 - 6 >= 0) {
            // the checking range is not cycling backwards of the starting position
            if (location2 >= location1 - 6 && location2 < location1) {       // second condition does not check for quality as then the player will be on top of the opponent which could be possible
            // on a safe spot
                return true;
            } else {
                return false;
            }
        } else {
            // the checking range is cycling backwards fo the starting position

            if (location2 < location1) {     // condition does not check for quality as then the player will be on top of the opponent which could be possible
                // first check ahead of the starting position
                return true;
            } else if (location2 <= GameConfiguration.GAME_CYCLE_END_INDEX && location2 >= GameConfiguration
                    .GAME_CYCLE_END_INDEX + location1 - 6 + 1) {       // added 1 since we start from 0
                // then check behind the starting position
                return true;
            } else {
                return false;
            }

        }
    }

    private boolean isPlayerInRangeInForwardDirection(int location1, int location2) {
        if (location1 + 6 <= GameConfiguration.GAME_CYCLE_END_INDEX) {
            // the checking range is not cycling forwards ahead of the starting position
            if (location2 <= location1 + 6 && location2 > location1) {
                return true;
            } else {
                return false;
            }
        } else {
            // the checking range is cycling forwards ahead of the starting position

            if (location2 <= GameConfiguration.GAME_CYCLE_END_INDEX && location2 > location1) {
                // first check behind of the starting position
                return true;
            } else if (location2 <= location1 + 6 - 1 - GameConfiguration.GAME_CYCLE_END_INDEX) {
            //subtracted 1 since we start from 0
                // then check ahead of the starting position
                return true;
            } else {
                return false;
            }

        }
    }

    private int getUpdatedLocation(int playerId, int tokenIndex, int numMoves) {
        int currentLocation = gameData.getPlayer(playerId).getPlayerToken(tokenIndex).getLocation();
        int finalLocation = currentLocation + numMoves;
        if (playerId == 0) {
            if (currentLocation == 0) {
                // special case since in this currentLocation + numMoves becomes less than GameConfiguration.GAME_CYCLE_END_INDEX
                if (!GameRules.playerBeatToEnterHome || gameData.getPlayer(playerId).isHasPlayerBeatenAToken()) {
                    finalLocation = gameData.getPlayer(playerId).getHomeRowStartIndex() + numMoves - 1;
                } else {
                    finalLocation = numMoves;
                }
            }
            // when it is the first player whose home row coincides with the board cycle
            if (currentLocation <= GameConfiguration.GAME_CYCLE_END_INDEX && currentLocation + numMoves > GameConfiguration.GAME_CYCLE_END_INDEX) {
                // can enter home row
                if (!GameRules.playerBeatToEnterHome || gameData.getPlayer(playerId).isHasPlayerBeatenAToken()) {
                    if (currentLocation == 0) {
                        finalLocation = gameData.getPlayer(playerId).getHomeRowStartIndex() + (numMoves - 1);
                    } else {
                        finalLocation = gameData.getPlayer(playerId).getHomeRowStartIndex() + (currentLocation + numMoves - GameConfiguration.GAME_CYCLE_END_INDEX - 2);   // 2 is subrated since
                        // we take cycle end as reference point instead of home row check as it is 0
                    }
                } else {
                    // restart cycle
                    finalLocation = finalLocation - GameConfiguration.GAME_CYCLE_END_INDEX - 1;   // 1 is subtracted since we start from 0
                }
            }
        } else {
            if (currentLocation <= GameConfiguration.GAME_CYCLE_END_INDEX && currentLocation + numMoves > GameConfiguration.GAME_CYCLE_END_INDEX) {
                // restart cycle
                finalLocation = finalLocation - GameConfiguration.GAME_CYCLE_END_INDEX - 1;   // 1 is subtracted since we start from 0
            } else if(currentLocation <= GameConfiguration.HOME_ROW_CHECK_INDEX_LIST[playerId] && currentLocation + numMoves > GameConfiguration.HOME_ROW_CHECK_INDEX_LIST[playerId]) {
                // can enter home row
                if (!GameRules.playerBeatToEnterHome || gameData.getPlayer(playerId).isHasPlayerBeatenAToken()) {
                    // enter home row
                    finalLocation = gameData.getPlayer(playerId).getHomeRowStartIndex() + (currentLocation + numMoves - GameConfiguration.HOME_ROW_CHECK_INDEX_LIST[playerId] - 1);
                }
            }
        }

        return finalLocation;
    }

}

