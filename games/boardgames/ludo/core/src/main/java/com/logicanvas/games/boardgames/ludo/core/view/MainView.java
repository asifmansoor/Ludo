package com.logicanvas.games.boardgames.ludo.core.view;

import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons.ButtonCreationException;
import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons.ThreeStateButton;
import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.MessageBar;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import com.logicanvas.games.boardgames.ludo.core.config.GameConfiguration;
import playn.core.Font;
import playn.core.Image;
import playn.core.Platform;
import playn.core.TextFormat;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;

/**
 * Created by amansoor on 18-09-2015.
 */
public class MainView extends GroupLayer {
    private MessageBar playerMessage;
    private MessageBar diceMeter;
    private MessageBar[] playerStats;
    private ThreeStateButton resetButton;
    private ThreeStateButton backButton;
    private CallBack callBackResetButton;
    private CallBack callBackBackButton;

    public MainView(Platform platform, float boardSize) {
        float width = platform.graphics().viewSize.width();
        float height = platform.graphics().viewSize.height();

        Image gamelogoImage = platform.assets().getImage("images/ludoLogo.jpg");
        ImageLayer gameLogoLayer = new ImageLayer(gamelogoImage);
        gameLogoLayer.setTranslation(0,0);
        gameLogoLayer.setSize((width - boardSize)/2, height - boardSize);
        this.add(gameLogoLayer);

        Image playerMsgBgImage = platform.assets().getImage("images/player_msg.png");
        ImageLayer playerMsgBgLayer = new ImageLayer(playerMsgBgImage);
        playerMsgBgLayer.setTranslation((width - boardSize)/2,height - boardSize - 0.1f*height);
        playerMsgBgLayer.setSize(boardSize, 0.1f*height);
        this.add(playerMsgBgLayer);

        Image playerStatsBgImage = platform.assets().getImage("images/player_stat.png");
        ImageLayer playerStatsBgLayer = new ImageLayer(playerStatsBgImage);
        playerStatsBgLayer.setTranslation(0,height - boardSize);
        playerStatsBgLayer.setSize((width - boardSize)/2, boardSize);
        this.add(playerStatsBgLayer);

        Image diceMeterBgImage = platform.assets().getImage("images/die_meter.png");
        ImageLayer diceMeterBgLayer = new ImageLayer(diceMeterBgImage);
        diceMeterBgLayer.setTranslation((width-boardSize)/2+boardSize,0);
        diceMeterBgLayer.setSize((width - boardSize)/2, height-boardSize);
        this.add(diceMeterBgLayer);

        Image resetButtonImage = platform.assets().getImage("images/menu/reset_button.png");
        try {
            resetButton = new ThreeStateButton(resetButtonImage.region(0, 100, 200, 100), resetButtonImage.region(0, 200, 200, 100),
                    resetButtonImage.region(0, 0, 200, 100), (width-boardSize)/2+boardSize, height - 0.32f*height, (width-boardSize)/2, 0.16f*height, new CallBack() {
                @Override
                public void call(Object data) {
                    if (callBackResetButton != null) {
                        callBackResetButton.call(null);
                    }
                }
            });
            this.add(resetButton);
            resetButton.activateButton();
        } catch (ButtonCreationException e) {
            e.printStackTrace();
        }

        Image backButtonImage = platform.assets().getImage("images/menu/back_button.png");
        try {
            backButton = new ThreeStateButton(backButtonImage.region(0, 100, 200, 100), backButtonImage.region(0, 200, 200, 100),
                    backButtonImage.region(0, 0, 200, 100), (width-boardSize)/2+boardSize, height - 0.16f*height, (width-boardSize)/2, 0.16f*height, new CallBack() {
                @Override
                public void call(Object data) {
                    if (callBackBackButton != null) {
                        callBackBackButton.call(null);
                    }
                }
            });
            this.add(backButton);
            backButton.activateButton();
        } catch (ButtonCreationException e) {
            e.printStackTrace();
        }

        playerMessage = new MessageBar(platform.graphics(), boardSize, (width - boardSize)/2 + 0.02f*width, height - boardSize - 0.07f*height, 0xFFFFFFFF,
                new TextFormat(new Font("Courier", Font.Style.PLAIN, 11)));
        this.add(playerMessage);

        diceMeter = new MessageBar(platform.graphics(), (width - boardSize)/2, (width-boardSize)/2+boardSize+(width - boardSize)/5, 0, 0xFFFFFFFF,
                new TextFormat(new Font("Courier", Font.Style.BOLD, 60)));
        this.add(diceMeter);

        playerStats = new MessageBar[GameConfiguration.NO_OF_PLAYERS];
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            playerStats[i] = new MessageBar(platform.graphics(), (width - boardSize) /2 - 0.02f*width, 0.02f*width, height - boardSize + 0.01f*height + i*0.2f*height, 0xFFFFFFFF,
                    new TextFormat(new Font("Courier", Font.Style.PLAIN, 11)));
            this.add(playerStats[i]);
            playerStats[i].showMessage(GameConfiguration.playerNames[i]+": ");
        }
    }

    public void showPlayerMessage(String text) {
        playerMessage.showMessage(text);
    }

    public void updateDiceCounter(int count) {
        if (count <= 0) {
            diceMeter.showMessage("");
        } else {
            diceMeter.showMessage("" + count);
        }
    }

    public void showPlayerStats(int playerId, String text) {
        playerStats[playerId].showMessage(text);
    }

    public void registerResetButtonListener(CallBack callBackImpl) {
        callBackResetButton = callBackImpl;
    }

    public void registerBackButtonListener(CallBack callBackImpl) {
        callBackBackButton = callBackImpl;
    }
}
