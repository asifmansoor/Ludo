package com.logicanvas.frameworks.boardgamesgdk.core.menu;

import com.logicanvas.frameworks.boardgamesgdk.core.config.BasicGameConfiguration;
import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons.ButtonCreationException;
import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons.ThreeStateButton;
import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.MessageBar;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import playn.core.*;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import pythagoras.f.IDimension;

/**
 * Created by amansoor on 15-09-2015.
 */
public class MenuView extends GroupLayer {

    private PlayerSelectButton[] playerSelectionButtons;
    private ThreeStateButton startButton;
    private MessageBar playerMessage;


    public MenuView(Platform platform, Menu menu, Image gamelogoImage) {
        final IDimension size = platform.graphics().viewSize;

        Image menuBgImage = platform.assets().getImage("images/menu/menu.jpg");
        ImageLayer menuBgLayer = new ImageLayer(menuBgImage);
        menuBgLayer.setSize(size);

        this.add(menuBgLayer);

        if(gamelogoImage != null) {
            //Image gamelogoImage = platform.assets().getImage("images/ludoLogo.jpg");
            ImageLayer gameLogoLayer = new ImageLayer(gamelogoImage);
            gameLogoLayer.setTranslation(size.width() / 2 - 0.21f * size.width() / 2, 0);
            gameLogoLayer.setSize(0.21f * size.width(), 0.66f * (0.21f * size.width()));
            this.add(gameLogoLayer);
        }

        playerMessage = new MessageBar(platform.graphics(), 0.34f*size.width(), size.width()/2 - 0.17f*size.width(), 0.27f*size.height(), 0xFF000000,
                new TextFormat(new Font("Courier", Font.Style.BOLD, 18)));
        this.add(playerMessage);
        playerMessage.showMessage("SELECT THE PLAYERS!");

        Image buttonsImage = platform.assets().getImage("images/menu/buttons.png");
        try {
            setupPlayerSelectButtons(buttonsImage, platform.graphics(), size.width()/2 - 0.25f*size.width(), 0.35f*size.height(), 0.25f*size.width(), 0.16f*size.height(), menu);
        } catch (ButtonCreationException e) {
            e.printStackTrace();
        }

        Image startButtonImage = platform.assets().getImage("images/menu/start_button.png");
        try {
            setupStartButton(startButtonImage, size.width() - 0.3f*size.width(), size.height() - 0.2f*size.height(), 0.3f*size.width(), 0.2f*size.height(), menu);
        } catch (ButtonCreationException e) {
            e.printStackTrace();
        }

    }

    private void setupPlayerSelectButtons(Image playerSelectButtonImage, Graphics gfx, float x, float y, float width, float height, final Menu menu) throws ButtonCreationException {
        playerSelectionButtons = new PlayerSelectButton[4];
        TextFormat format = new TextFormat(new Font("Courier", Font.Style.BOLD, 24));
        TextWrap wrap = new TextWrap(100);
        String[][] texts = {{"OFF", "CPU", "Player1"},{"OFF", "CPU", "Player2"},{"OFF", "CPU", "Player3"},{"OFF", "CPU", "Player4"},};

        playerSelectionButtons[0] = new PlayerSelectButton(playerSelectButtonImage.region(0, 0, 150, 80),
                playerSelectButtonImage.region(0, 80, 150, 80), x, y, width, height, gfx, format, wrap, texts[0], new CallBack() {
            @Override
            public void call(Object data) {
                menu.updatePlayerCaption(0, playerSelectionButtons[0].getTextIndex());
            }
        });
        this.add(playerSelectionButtons[0]);
        playerSelectionButtons[1] = new PlayerSelectButton(playerSelectButtonImage.region(150, 80, 150, 80),
                playerSelectButtonImage.region(150, 160, 150, 80), x + width + 5, y, width, height, gfx, format, wrap, texts[1], new CallBack() {
            @Override
            public void call(Object data) {
                menu.updatePlayerCaption(1, playerSelectionButtons[1].getTextIndex());
            }
        });
        this.add(playerSelectionButtons[1]);
        playerSelectionButtons[2] = new PlayerSelectButton(playerSelectButtonImage.region(0, 160, 150, 80),
                playerSelectButtonImage.region(150, 0, 150, 80), x + width + 5, y + height + 5, width, height, gfx, format, wrap, texts[2], new CallBack() {
            @Override
            public void call(Object data) {
                menu.updatePlayerCaption(2, playerSelectionButtons[2].getTextIndex());
            }
        });
        this.add(playerSelectionButtons[2]);
        playerSelectionButtons[3] = new PlayerSelectButton(playerSelectButtonImage.region(0, 240, 150, 80),
                playerSelectButtonImage.region(150, 240, 150, 80), x, y + height + 5, width, height, gfx, format, wrap, texts[3], new CallBack() {
            @Override
            public void call(Object data) {
                menu.updatePlayerCaption(3, playerSelectionButtons[3].getTextIndex());
            }
        });
        this.add(playerSelectionButtons[3]);
    }

    public void resetPlayerButtons() {
        for (int i = 0; i < BasicGameConfiguration.NO_OF_PLAYERS; i++) {
            playerSelectionButtons[i].resetButton();
        }
    }

    private void setupStartButton(Image startButtonImage, float x, float y, float width, float height, final Menu menu) throws ButtonCreationException {
        startButton = new ThreeStateButton(startButtonImage.region(0, 100, 200, 100), startButtonImage.region(0, 200, 200, 100),
                startButtonImage.region(0, 0, 200, 100), x, y, width, height, new CallBack() {
            @Override
            public void call(Object data) {
                menu.startGame();
            }
        });
        startButton.deactivateButton();
        this.add(startButton);
    }

    public int getPlayerTypeSelected(int playerId) {
        return playerSelectionButtons[playerId].getTextIndex();
    }

    public void activateStartButton() {
        startButton.activateButton();
    }

    public void deactivateStartButton() {
        startButton.deactivateButton();
    }

}
