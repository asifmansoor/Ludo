package com.logicanvas.games.boardgames.ludo.ui.Widgets;

import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.BaseWidget;
import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.MessageBar;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import com.logicanvas.games.boardgames.ludo.config.GameConfiguration;
import playn.core.Font;
import playn.core.Graphics;
import playn.core.Image;
import playn.core.TextFormat;
import playn.scene.ImageLayer;
import playn.scene.Pointer;

/**
 * Created by amansoor on 18-09-2015.
 */
public class PlayerWidget extends BaseWidget {
    private ImageLayer[] tokenLayers;
    private ImageLayer playerHighlight;
    private ImageLayer[] tokenHighlight;
    private MessageBar caption;
    private float tokenHightLightOffset;

    public PlayerWidget(Image tokenImage, float tokenXOffset, float tokenYOffset, float tokenImageWidth,
                        Image playerHighlightImage, Image tokenHighlightImage, Graphics gfx,
                        float boardWidth, float captionX, float captionY, final CallBack callBackImpl) {

        tokenHightLightOffset = tokenImageWidth/10;
        playerHighlight = new ImageLayer(playerHighlightImage);
        playerHighlight.setSize(boardWidth, boardWidth);
        playerHighlight.setTranslation(captionX - 0.05f*boardWidth, captionY);
        playerHighlight.setVisible(false);
        this.add(playerHighlight);

        tokenLayers = new ImageLayer[GameConfiguration.NO_OF_TOKENS_PER_PLAYER];
        tokenHighlight = new ImageLayer[GameConfiguration.NO_OF_TOKENS_PER_PLAYER];
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            Image.Region tokenRegion = tokenImage.region(tokenXOffset, tokenYOffset, GameConfiguration.TOKEN_SIZE,
                    GameConfiguration.TOKEN_SIZE);
            tokenLayers[i] = new ImageLayer(tokenRegion);
            tokenLayers[i].setSize(tokenImageWidth, tokenImageWidth);
            final int finalI = i;
            tokenLayers[i].events().connect(new Pointer.Listener() {
                @Override
                public void onEnd(Pointer.Interaction iact) {
                    super.onEnd(iact);
                    callBackImpl.call(finalI);
                }
            });
            this.add(tokenLayers[i]);

            tokenHighlight[i] = new ImageLayer(tokenHighlightImage);
            tokenHighlight[i].setSize(tokenImageWidth*6/5, tokenImageWidth*6/5);
            tokenHighlight[i].setVisible(false);
            this.add(tokenHighlight[i]);
        }

        caption = new MessageBar(gfx, 0.3f*boardWidth, captionX, captionY, 0xFF000000, new TextFormat(new Font("Courier", Font.Style.PLAIN, 11)));
        this.add(caption);
    }

    public void updateTokenLocation(int index, int x, int y) {
        tokenLayers[index].setTx(x);
        tokenLayers[index].setTy(y);
    }

/*
    public float getTokenPosX(int index) {
        return tokenLayers[index].tx();
    }
*/

    public void setCaption(String text) {
        caption.showMessage(text);
    }

    public void showPlayerHighlightMarker(boolean show) {
        playerHighlight.setVisible(show);
    }

    public void showTokenHighlightMarker(int tokenId, boolean show) {
        tokenHighlight[tokenId].setTx(tokenLayers[tokenId].tx() - tokenHightLightOffset);
        tokenHighlight[tokenId].setTy(tokenLayers[tokenId].ty() - tokenHightLightOffset);
        tokenHighlight[tokenId].setVisible(show);
    }

    public void hideAllTokenHighlightMarker() {
        for (int i = 0; i < GameConfiguration.NO_OF_TOKENS_PER_PLAYER; i++) {
            tokenHighlight[i].setVisible(false);
        }
    }
}
