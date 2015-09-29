package com.logicanvas.games.boardgames.ludo.core;

import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesBasicCore;
import com.logicanvas.frameworks.boardgamesgdk.core.dice.Dice;
import com.logicanvas.frameworks.boardgamesgdk.core.dice.DiceView;
import com.logicanvas.frameworks.boardgamesgdk.core.gameplay.BasicPlayer;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.BoardGamesLogger;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.GameTimer;
import com.logicanvas.games.boardgames.ludo.core.audio.LudoSounds;
import com.logicanvas.games.boardgames.ludo.core.config.GameConfiguration;
import com.logicanvas.games.boardgames.ludo.core.intelligence.*;
import com.logicanvas.games.boardgames.ludo.core.model.GameData;
import com.logicanvas.games.boardgames.ludo.core.model.PlayerToken;
import com.logicanvas.games.boardgames.ludo.core.view.LudoView;
import com.logicanvas.games.boardgames.ludo.core.view.MainView;
import playn.core.Clock;
import playn.core.Image;
import playn.core.Platform;

import java.util.ArrayList;
import java.util.Iterator;

public class Ludo extends BoardGamesBasicCore {

    private MainView mainView;
    private LudoView boardView;
    private DiceView diceView;
    private Dice dice;
    private GameData gameData;
    private MoveEvaluator moveEvaluator;
    private boolean gameInProgress;
    private int lastUpdate = 0;
    private GameOriginator gameOriginator;
    private LudoRecorder recorder;
    private LudoSounds ludoSounds;

    public Ludo(final Platform plat) {
        super(plat, 33, GameConfiguration.NO_OF_PLAYERS, GameConfiguration.DEBUG); // update our "simulation" 33ms (30 times per second)


        float aspectRatio = viewSize.width()/viewSize.height();
        float boardSize;
        // setup board size
        if (aspectRatio >= GameConfiguration.FULL_HEIGHT) {
            boardSize = 0.8f*plat.graphics().viewSize.height();
        } else {
            boardSize = 0.6f*plat.graphics().viewSize.width();
        }

        // setup players
        ludoSounds = new LudoSounds(plat);
        setupGame();

        //Setup the board
        boardView = new LudoView(plat, boardSize, new CallBack() {
            @Override
            public void call(Object data) {
                int[] element = (int[]) data;
                if (element[0] == gameData.getTurn()) {
                    gameData.setPlayerMove(element[1]);
                    gameData.setGameState(GameConfiguration.GAME_STATE.INPUT_RECD);
                }
            }
        });

        diceView = new DiceView(plat, boardSize);
        diceView.registerDiceClickListener(new CallBack() {
            @Override
            public void call(Object data) {
                if (gameData.getGameState() == GameConfiguration.GAME_STATE.WAITING_FOR_SPIN) {
                    gameData.setGameState(GameConfiguration.GAME_STATE.SPIN_DIE);
                    playDiceAnimation();
                }
            }
        });
        diceView.registerDiceAnimEndListener(new CallBack() {
            @Override
            public void call(Object data) {
                postSpinProcessing();
            }
        });


        mainView = new MainView(plat, boardSize);
        mainView.registerResetButtonListener(new CallBack() {
            @Override
            public void call(Object data) {
                mainView.showPlayerMessage("Game reseting!");
                gameInProgress = false;
                gameData.setGameState(GameConfiguration.GAME_STATE.GAME_END);
                GameTimer.runAfter(500, new CallBack() {
                    @Override
                    public void call(Object data) {
                        resetGame();
                    }
                });
            }
        });
        mainView.registerBackButtonListener(new CallBack() {
            @Override
            public void call(Object data) {
                mainView.showPlayerMessage("Game Closing!");
                gameData.setGameState(GameConfiguration.GAME_STATE.GAME_END);
                GameTimer.runAfter(500, new CallBack() {
                    @Override
                    public void call(Object data) {
                        closeGame();
                    }
                });
            }
        });

        showMainView(false);
        rootLayer.add(boardView);
        rootLayer.add(mainView);
        rootLayer.add(diceView);

        // setup menu
        Image gamelogoImage = plat.assets().getImage("images/ludoLogo.jpg");
        initMenu(gamelogoImage);
    }

    private void playDiceAnimation() {
        mainView.updateDiceCounter(0);
        diceView.playDieAnimation(gameData.getDiceRoll());
    }



    private void showMainView(boolean show) {
        boardView.setVisible(show);
        mainView.setVisible(show);
        diceView.setVisible(show);
        diceView.showDice(3);
    }

    private void setPlayerCaptionStrings() {
        String[] text_options = {"OFF", "COMPUTER AI", "PLAYER "};
        String text;
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            text = text_options[gameData.getPlayer(i).getPlayerType()];
            if (gameData.getPlayer(i).getPlayerType() == BasicPlayer.PLAYER_TYPE_HUMAN) {
                text += i+1;
            }
            boardView.setPlayerCaption(i, text);
        }
    }

    private void initPlayerStats() {
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            updatePlayerStats(i);
        }
    }

    public void quitGame() {
        //
    }

    public void updateSelectedPlayerType(int playerId, int playerType) {
        gameData.getPlayer(playerId).setPlayerType(playerType);
    }

    @Override
    public void closeGame() {
        gameData.setGameState(GameConfiguration.GAME_STATE.GAME_END);
        resetGame();
        gameInProgress = false;
        boardView.showMask(false);
        menuView.setVisible(true);
        showMainView(false);
    }

    @Override
    public void startGame() {
        menuView.setVisible(false);

        setPlayerCaptionStrings();
        initPlayerStats();
        showMainView(true);


        // Update initial board display
        updateBoard();
        boardView.showMask(true);
        gameInProgress = true;
    }

    private void resetGame() {
        GameTimer.removeAllListeners();
        boardView.hideAllPlayerHighlightMarker();
        boardView.hideAllTokenHighlightMarker(gameData.getTurn());
        gameData.setRound(0);
        gameData.setTurn(-1);
        gameData.getMoveSequence().resetSequence();
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            gameData.getPlayer(i).resetPlayer();
        }
        gameData.setGameState(GameConfiguration.GAME_STATE.START);
        gameInProgress = true;
    }

    private void setupGame() {
        gameData = new GameData();
        LudoPlayer[] players = new LudoPlayer[GameConfiguration.NO_OF_TOKENS_PER_PLAYER];

        // setup blue player data
        players[GameConfiguration.BLUE_PLAYER_ID] = new LudoPlayer(GameConfiguration.BLUE_PLAYER_ID, GameConfiguration.BLUE_START_INDEX,
                GameConfiguration.BLUE_OPEN_INDEX, GameConfiguration.BLUE_HOME_ROW_START_INDEX);
        players[GameConfiguration.RED_PLAYER_ID] = new LudoPlayer(GameConfiguration.RED_PLAYER_ID, GameConfiguration.RED_START_INDEX,
                GameConfiguration.RED_OPEN_INDEX, GameConfiguration.RED_HOME_ROW_START_INDEX);
        players[GameConfiguration.GREEN_PLAYER_ID] = new LudoPlayer(GameConfiguration.GREEN_PLAYER_ID, GameConfiguration.GREEN_START_INDEX,
                GameConfiguration.GREEN_OPEN_INDEX, GameConfiguration.GREEN_HOME_ROW_START_INDEX);
        players[GameConfiguration.YELLOW_PLAYER_ID] = new LudoPlayer(GameConfiguration.YELLOW_PLAYER_ID, GameConfiguration.YELLOW_START_INDEX,
                GameConfiguration.YELLOW_OPEN_INDEX, GameConfiguration.YELLOW_HOME_ROW_START_INDEX);

        gameData.setPlayers(players);

        // initialize the player moves tracker
        gameData.setMoveSequence(new MoveSequence());
        moveEvaluator = new MoveEvaluator(gameData);

        recorder = new LudoRecorder(gameData, plat);

        dice = new Dice();

        gameInProgress = false;
        gameData.setTurn(-1);
        gameData.setGameState(GameConfiguration.GAME_STATE.START);
        gameData.setRound(0);

//        gameOriginator = new GameOriginator(gameData);

    }

    /*
     * This method is to update the display of all tokens on the board with their updated locations
     */
    private void updateBoard() {
        for (int playerId = 0; playerId < GameConfiguration.NO_OF_PLAYERS; playerId++) {
            for (int tokenIndex = 0; tokenIndex < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; tokenIndex++) {
                int x = GameConfiguration.BOARD_POSITIONS[gameData.getPlayer(playerId).getPlayerToken(tokenIndex)
                        .getLocation()][0];
                int y = GameConfiguration.BOARD_POSITIONS[gameData.getPlayer(playerId).getPlayerToken(tokenIndex)
                        .getLocation()][1];
                //LudoLogger.log("Show token: playerid: "+playerId+" token: "+tokenIndex+" x: "+x+" y: "+y);
                boardView.updateTokenLocation(playerId, tokenIndex, x, y);
            }
        }

    }

    @Override
    public void update(Clock clock) {
        super.update(clock);
        GameTimer.update(clock);
        int elapsed = clock.tick - lastUpdate;
        if (gameInProgress) {
            if (elapsed > 500) {
                gameUpdate();
                lastUpdate = clock.tick;
            }
        }

    }

    private void gameUpdate() {
        //LudoLogger.log("Game state: "+gameData.getGameState());
        switch (gameData.getGameState()) {
            case START:
                if (nextTurn()) {
                    boardView.showPlayerHighlightMarker(gameData.getTurn(), true);
                    gameData.setDiceRoll(dice.oneDiceOutcome());
                    if (gameData.getPlayer(gameData.getTurn()).getPlayerType() == BasicPlayer.PLAYER_TYPE_HUMAN) {
                        gameData.setGameState(GameConfiguration.GAME_STATE.WAITING_FOR_SPIN);
                    } else {
                        playDiceAnimation();
                    }
                    //getOutcome();
                }
                break;
            case WAITING_FOR_SPIN:
                break;
            case SPIN_DIE:
                break;
            case WAITING_FOR_INPUT:
                // don't do anything
                break;
            case INPUT_RECD:
                setupMove();
                break;
            case PLAY_MOVE:
                playMove();
                break;
            case PLAY_END:
                mainView.showPlayerMessage("");
                boardView.showPlayerHighlightMarker(gameData.getTurn(), false);
                gameData.setGameState(GameConfiguration.GAME_STATE.START);
                break;
            case GAME_END:
                break;
        }
    }

/*    private void getOutcome() {
        gameData.setDiceRoll(dice.oneDiceOutcome());
        diceView.showDice(gameData.getDiceRoll());
        ludoSounds.playSound(GameConfiguration.GAME_SOUNDS_DICE_ROLL);

        if (gameData.getPlayer(gameData.getTurn()).getPlayerType() == GameConfiguration.PLAYER_TYPE_HUMAN) {
            if (moveEvaluator.isThereAnyValidMoveForPlayer()) {
                ludoText.showMessage(GameConfiguration.playerNames[gameData.getTurn()]+" player. Choose token to play!");
            } else {
                ludoText.showMessage("No valid Move. \nClick to continue!");
            }
            gameData.setGameState(GameConfiguration.GAME_STATE.WAITING_FOR_SPIN);
            boardView.showMask(false);
        } else {
            gameData.setGameState(GameConfiguration.GAME_STATE.INPUT_RECD);
        }

    }*/

    private void postSpinProcessing() {
        mainView.updateDiceCounter(gameData.getDiceRoll());
        // check if input is required
        if (gameData.getPlayer(gameData.getTurn()).getPlayerType() == BasicPlayer.PLAYER_TYPE_HUMAN) {
            GameMove move = moveEvaluator.evaluateMove();
            highlightPlayerTokens();
            if (move.getMoveType() != GameMove.IDLE) {
                mainView.showPlayerMessage(GameConfiguration.playerNames[gameData.getTurn()] + " player. Choose token to play!");
                gameData.setGameState(GameConfiguration.GAME_STATE.WAITING_FOR_INPUT);
            } else {
                mainView.showPlayerMessage("No valid Move!");
                GameTimer.runAfter(1000, new CallBack() {
                    @Override
                    public void call(Object data) {
                        gameData.setGameState(GameConfiguration.GAME_STATE.INPUT_RECD);
                    }
                });
            }
            boardView.showMask(false);
        } else {
            gameData.setGameState(GameConfiguration.GAME_STATE.INPUT_RECD);
        }
    }

    /*
     * Method to setup current move
     */
    private void setupMove() {
        GameMove move;
        mainView.showPlayerMessage("");
//        diceView.hideDice();
        boardView.showMask(true);
        if (gameData.getPlayer(gameData.getTurn()).getPlayerType() == BasicPlayer.PLAYER_TYPE_HUMAN) {
            if (moveEvaluator.isThereAnyValidMoveForPlayer()) {
                move = moveEvaluator.evaluateMove(gameData.getPlayerMove());
                if (move.getMoveType() == GameMove.IDLE) {
                    mainView.showPlayerMessage("Invalid token! Select a different one.");
                    ludoSounds.playSound(GameConfiguration.GAME_SOUNDS_INVALID_PICK);
                    gameData.setGameState(GameConfiguration.GAME_STATE.WAITING_FOR_INPUT);
                    boardView.showMask(false);
                    return;
                } else {
                    ludoSounds.playSound(GameConfiguration.GAME_SOUNDS_PLAYER_TOUCH);
                }
            } else {
                gameData.setGameState(GameConfiguration.GAME_STATE.START);
                return;
            }
        } else {
            move = moveEvaluator.evaluateMove();
            highlightPlayerTokens();
        }
        PlayerToken hitToken = move.getHitOpponent();
        if (hitToken != null) {
            GameMove hitMove = new GameMove(hitToken.getPlayerId(), hitToken.getIndex(), GameMove.RESET, -1, 0, -1);
            gameData.getMoveSequence().addSequence(hitMove);
        }

        gameData.getMoveSequence().addSequence(move);
        gameData.setGameState(GameConfiguration.GAME_STATE.PLAY_MOVE);
    }

    private void highlightPlayerTokens() {
        ArrayList<Integer> moveOpts = gameData.getPlayerMoveOptions();
        if (moveOpts != null && !moveOpts.isEmpty()) {
            Iterator<Integer> itr = moveOpts.iterator();
            while (itr.hasNext()) {
                int obj = itr.next();
                boardView.showTokenHighlightMarker(gameData.getTurn(), obj, true);
            }
        }
    }

    private void restoreGame() {
        BoardGamesLogger.debug("Turn :" + gameData.getTurn());
        BoardGamesLogger.debug("Dice :" + gameData.getDiceRoll());
        gameData.getMoveSequence().addSequence(moveEvaluator.evaluateMove());
        playMove();
    }

    private void playMove() {
        GameMove gameMove;
        boardView.hideAllTokenHighlightMarker(gameData.getTurn());
        while (!gameData.getMoveSequence().isEmpty()) {
            gameMove = gameData.getMoveSequence().popMove();
            gameData.getPlayer(gameMove.getPlayerId()).playMove(gameMove);
            if (gameMove.getMoveType() != GameMove.IDLE) {
                ludoSounds.playSound(GameConfiguration.GAME_SOUNDS_TOKEN_MOVE);
            }
            //printMoveText(gameMove.getMoveType(), gameMove.getFinalLocation());
            updateBoard();
        }
        updatePlayerStats(gameData.getTurn());
        gameData.setGameState(GameConfiguration.GAME_STATE.PLAY_END);
    }

    private boolean nextTurn() {
        if (checkGameEnd()) {
            gameData.setGameState(GameConfiguration.GAME_STATE.GAME_END);
            BoardGamesLogger.log("Game Over!");
            return false;
        } else {
            gameData.setTurn(gameData.getTurn() + 1);
            if (gameData.getTurn() > 3) {
                gameData.setTurn(0);
                gameData.setRound(gameData.getRound()+1);   // update the round
                System.out.println("");
            }
/*
            if (!recorder.saveGame()) {
                plat.log().debug("Unable to save");
            }
*/

            if (gameData.getPlayer(gameData.getTurn()).isAllHomeForPlayer() || gameData.getPlayer(gameData.getTurn()).getPlayerType() == BasicPlayer.PLAYER_TYPE_OFF) {
                nextTurn();
            }
            BoardGamesLogger.debug("Turn :" + gameData.getTurn());
            if (gameData.getPlayer(gameData.getTurn()).getPlayerType() == BasicPlayer.PLAYER_TYPE_HUMAN) {
                mainView.showPlayerMessage(GameConfiguration.playerNames[gameData.getTurn()] + " player turn. Click Dice to roll.");
            } else {
                mainView.showPlayerMessage(GameConfiguration.playerNames[gameData.getTurn()] + " player turn.");
            }
            return true;
        }

    }

    private boolean checkGameEnd() {
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            if (gameData.getPlayer(i).isAllHomeForPlayer()) {
                mainView.showPlayerMessage("Game Over! " + GameConfiguration.playerNames[i] + " player wins.");
                return true;
            }
        }

        return false;
    }

/*
    private void printMoveText(int moveType, int finalLocation) {
        ludoText.addText(GameConfiguration.playerNames[gameData.getTurn()]+": "+GameConfiguration.moveTypeNames[moveType]+" - roll: "+gameData.getDiceRoll()+" : loc: "+ finalLocation+"\n");
    }
*/

    private void updatePlayerStats(int playerId) {
        LudoPlayer player = gameData.getPlayer(playerId);
        int inHomeTokens = player.getNumPlayerTokensInState(GameConfiguration.TOKEN_STATE_HOME);
        int unOpenedTokens = player.getNumPlayerTokensInState(GameConfiguration.TOKEN_STATE_UNOPEN);
        switch (player.getPlayerType()) {
            case BasicPlayer.PLAYER_TYPE_OFF:
                mainView.showPlayerStats(playerId, GameConfiguration.playerNames[playerId]+ ": OFF");
                break;
            case BasicPlayer.PLAYER_TYPE_AI_LEVEL1:
                mainView.showPlayerStats(playerId, GameConfiguration.playerNames[playerId]+": AI\n\t# home: "
                        +inHomeTokens+"\n\t# unopened: "+unOpenedTokens+"\n\t# on board: "
                        +(GameConfiguration.NO_OF_TOKENS_PER_PLAYER - inHomeTokens - unOpenedTokens));
                break;
            case BasicPlayer.PLAYER_TYPE_HUMAN:
                mainView.showPlayerStats(playerId, GameConfiguration.playerNames[playerId]+": Player"+(playerId+1)+"\n\t# home: "
                        +inHomeTokens+"\n\t# unopened: "+unOpenedTokens+"\n\t# on board: "
                        +(GameConfiguration.NO_OF_TOKENS_PER_PLAYER - inHomeTokens - unOpenedTokens));
                break;
        }
    }
}
