package com.logicanvas.boardgames.ludo.menu;

import com.logicanvas.boardgames.ludo.config.GameConfiguration;
import com.logicanvas.boardgames.ludo.core.Ludo;
import playn.core.Platform;

/**
 * Created by amansoor on 16-09-2015.
 */
public class Menu {
    private MenuView view;
    private Platform platform;
    private Ludo game;

    public Menu(Platform platform, Ludo game) {
        this.platform = platform;
        this.game = game;

        view = new MenuView(platform, this);
    }

    public void init() {
        //
    }

    public void resetPlayerButtons() {
        view.resetPlayerButtons();
    }

    public void startGame() {
        game.startGame();
    }

    public void updatePlayerCaption(int playerId, int playerType) {
        game.updateSelectedPlayerType(playerId, playerType);
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
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            if (view.getPlayerTypeSelected(i) != GameConfiguration.PLAYER_TYPE_OFF) {
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
