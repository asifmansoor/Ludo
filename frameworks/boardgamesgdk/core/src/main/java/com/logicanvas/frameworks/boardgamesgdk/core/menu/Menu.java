package com.logicanvas.frameworks.boardgamesgdk.core.menu;

import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesBasicCore;
import com.logicanvas.frameworks.boardgamesgdk.core.config.BasicGameConfiguration;
import com.logicanvas.frameworks.boardgamesgdk.core.gameplay.BasicPlayer;
import playn.core.Image;
import playn.core.Platform;

/**
 * Created by amansoor on 16-09-2015.
 */
public class Menu {
    private MenuView view;
    private Platform platform;
    private BoardGamesBasicCore gameCore;

    public Menu(Platform platform, BoardGamesBasicCore gameCore, Image gamelogoImage) {
        this.platform = platform;
        this.gameCore = gameCore;

        view = new MenuView(platform, this, gamelogoImage);
    }

    public void init() {
        //
    }

    public void resetPlayerButtons() {
        view.resetPlayerButtons();
    }

    public void startGame() {
        gameCore.startGame();
    }

    public void updatePlayerCaption(int playerId, int playerType) {
        gameCore.updateSelectedPlayerType(playerId, playerType);
        if (isGameReadyToPlay()) {
            view.activateStartButton();
        } else {
            view.deactivateStartButton();
        }
    }

    public MenuView getView() {
        return view;
    }

    private boolean isGameReadyToPlay() {
        int numPlayersSelected = 0;
        for (int i = 0; i < BasicGameConfiguration.NO_OF_PLAYERS; i++) {
            if (view.getPlayerTypeSelected(i) != BasicPlayer.PLAYER_TYPE_OFF) {
                numPlayersSelected++;
            }
        }

        if (numPlayersSelected >= 2) {
            return true;
        } else {
            return false;
        }
    }
}
