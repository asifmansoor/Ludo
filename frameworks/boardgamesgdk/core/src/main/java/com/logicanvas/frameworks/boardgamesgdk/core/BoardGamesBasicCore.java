package com.logicanvas.frameworks.boardgamesgdk.core;

import com.logicanvas.frameworks.boardgamesgdk.core.config.BasicGameConfiguration;
import com.logicanvas.frameworks.boardgamesgdk.core.gameplay.BasicPlayer;
import com.logicanvas.frameworks.boardgamesgdk.core.menu.Menu;
import com.logicanvas.frameworks.boardgamesgdk.core.menu.MenuView;
import com.logicanvas.frameworks.boardgamesgdk.core.model.BasicGameModel;
import playn.core.Image;
import playn.core.Platform;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;

public abstract class BoardGamesBasicCore extends SceneGame {

    protected IDimension viewSize;
    protected BasicGameModel basicGameModel;
    protected Menu menu;
    protected MenuView menuView;

    public BoardGamesBasicCore(Platform plat, int updateRate, int numPlayers, boolean debug) {
        super(plat, 33); // update our "simulation" 33ms (30 times per second)

        BasicGameConfiguration.DEBUG = debug;
        BasicGameConfiguration.NO_OF_PLAYERS = numPlayers;

        basicGameModel = new BasicGameModel();
        initializePlayers();

        Pointer pointer = new Pointer(plat, rootLayer, false);
        plat.input().mouseEvents.connect(new Mouse.Dispatcher(rootLayer, false));

        viewSize = plat.graphics().viewSize;

        plat.log();
    }

    public void initMenu(Image gamelogoImage) {
        // setup menu
        menu = new Menu(plat, this, gamelogoImage);
        menuView = menu.getView();
        rootLayer.add(menuView);
    }

    private void initializePlayers() {
        BasicPlayer[] players = new BasicPlayer[BasicGameConfiguration.NO_OF_PLAYERS];
        for (int i = 0; i < BasicGameConfiguration.NO_OF_PLAYERS; i++) {
            players[i] = new BasicPlayer(i);
        }
        basicGameModel.setPlayers(players);
    }

    public void updateSelectedPlayerType(int playerId, int playerType) {
        BasicPlayer player = basicGameModel.getPlayers(playerId);
        if (player != null) {
            player.setPlayerType(playerType);
        }
    }

    public abstract void startGame();

    public abstract void closeGame();
}
