package com.logicanvas.boardgames.ludo.core;

import com.logicanvas.boardgames.ludo.config.GameConfiguration;
import com.logicanvas.boardgames.ludo.model.GameData;
import com.logicanvas.boardgames.ludo.view.LudoView;
import playn.scene.ImageLayer;

/**
 * Created by amansoor on 04-09-2015.
 */
public class LudoInput {
    private GameData gameData;
    LudoView view;

    public LudoInput(GameData gameData, LudoView view) {
        this.gameData = gameData;
        this.view = view;
    }

    public void processInput(int playerId, int index) {
        if (gameData.getTurn() == playerId && gameData.getGameState() == GameConfiguration.GAME_STATE.WAITING_FOR_INPUT) {
            gameData.setPlayerMove(index);
            gameData.setGameState(GameConfiguration.GAME_STATE.INPUT_RECD);
        }
    }

    public boolean processInput(float x, float y) {
/*
        x -= view.getDiff();
        int playerId = gameData.getTurn();
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            int tokenLocation = gameData.getPlayer(playerId).getPlayerToken(i).getLocation();
            ImageLayer playerTokenLayer = ((ImageLayer[])view.getLayers().get(playerId))[i];
            float posX = playerTokenLayer.tx();
            float posY = playerTokenLayer.ty();

            if (x >= posX && y >= posY && x <= posX + view.getAdjustedTokenImageWidth() && y <= posY + view.getAdjustedTokenImageWidth()) {
                gameData.setPlayerMove(i);
                gameData.setGameState(GameConfiguration.GAME_STATE.INPUT_RECD);
                return true;
            }
        }
*/

        return false;
    }

}
