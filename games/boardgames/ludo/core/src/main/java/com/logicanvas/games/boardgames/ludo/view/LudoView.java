package com.logicanvas.games.boardgames.ludo.view;

import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import com.logicanvas.games.boardgames.ludo.config.GameConfiguration;
import com.logicanvas.games.boardgames.ludo.ui.Widgets.PlayerWidget;
import playn.core.Image;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import pythagoras.f.IDimension;

/**
 * Created by amansoor on 17-06-2015.
 * <p/>
 * This class is for displaying the ludo board and the player tokens
 */
public class LudoView extends GroupLayer{
    private float adjustedTokenImageWidth;
    private float initialSize = 600f;
    private float finalSize;

    private PlayerWidget[] players;
//    private MessageBar playerMessage;

    private ImageLayer mask;

    private Platform platform;

    public LudoView(Platform platform, float boardSize, final CallBack callBack) {
        this.platform = platform;
        final IDimension size = platform.graphics().viewSize;

        platform.log().debug("View size: "+size.width()+" - "+size.height());

        this.finalSize = boardSize;

        Image bgImage = platform.assets().getImage("images/background.jpg");
        ImageLayer bgLayer = new ImageLayer(bgImage);
        bgLayer.setSize(size.width(), size.height());
        this.add(bgLayer);

        Image boardImage = platform.assets().getImage("images/ludo_big.jpg");
        ImageLayer boardLayer = new ImageLayer(boardImage);
        // scale the background to fill the screen
        boardLayer.setSize(finalSize, finalSize);


        adjustedTokenImageWidth = finalSize*0.06f;
        setupPlayerWidgets(callBack);

        Image maskImage = platform.assets().getImage("images/mask.png");
        mask = new ImageLayer(maskImage);
        mask.setSize(finalSize, finalSize);
        mask.setVisible(false);

        float boardPosX = (size.width() - finalSize) / 2;
        float boardPosY = (size.height() - finalSize);
        //playerMessage = new MessageBar(platform.graphics(), finalSize, 0, -20, 0xFF0000FF, new TextFormat(new Font("Courier", Font.Style.PLAIN, 11)));

        GroupLayer mainLayer = new GroupLayer(finalSize, finalSize);
        mainLayer.add(boardLayer);
        mainLayer.add(players[0]);
        mainLayer.add(players[1]);
        mainLayer.add(players[2]);
        mainLayer.add(players[3]);
        mainLayer.add(mask);
        //mainLayer.add(playerMessage);

        this.addAt(mainLayer, boardPosX, boardPosY);
        platform.log().debug("Main layer: "+mainLayer.width()+" - "+mainLayer.height());
    }

    private void setupPlayerWidgets(final CallBack callback) {
        Image tokenImage = platform.assets().getImage("images/tokens.png");
        Image playerHighlightImage = platform.assets().getImage("images/selected_player_marker.png");
        Image tokenHighlightImage = platform.assets().getImage("images/token_marker.png");
        float[][] captionPos = {{0.05f, 0},{0.65f, 0},{0.65f, 0.60f},{0.05f, 0.60f}};
        int [][] tokenRegionPos = {{0,0},{GameConfiguration.TOKEN_SIZE, 0},{GameConfiguration.TOKEN_SIZE, GameConfiguration.TOKEN_SIZE},{0, GameConfiguration.TOKEN_SIZE}};
        players = new PlayerWidget[GameConfiguration.NO_OF_PLAYERS];
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            final int finalI = i;
            players[i] = new PlayerWidget(tokenImage, tokenRegionPos[i][0], tokenRegionPos[i][1], adjustedTokenImageWidth,
                    playerHighlightImage, tokenHighlightImage, platform.graphics(), finalSize, captionPos[i][0]*finalSize, captionPos[i][1]*finalSize, new CallBack() {
                @Override
                public void call(Object data) {
                    int receivedElement = (int) data;
                    int[] element = new int[2];
                    element[0] = finalI;
                    element[1] = receivedElement;
                    callback.call(element);
                }
            });
        }
    }

    public void setPlayerCaption(int playerId, String text) {
        players[playerId].setCaption(text);
    }

    public void updateTokenLocation(int playerId, int index, int x, int y) {
        x *= calcScalFactor();
        y *= calcScalFactor();
        //platform.log().debug("Updatelocation: player: "+playerId+" index: "+index+" x: "+x+" y: "+y);

        players[playerId].updateTokenLocation(index, x, y);
    }

    private float calcScalFactor() {
        float factor = 0;
        factor = finalSize/initialSize;
        return factor;
    }

    public void showMask(boolean visible) {
        mask.setVisible(visible);
    }

    public void showPlayerHighlightMarker(int playerId, boolean show) {
        players[playerId].showPlayerHighlightMarker(show);
    }

    public void hideAllPlayerHighlightMarker() {
        for (int i = 0; i < GameConfiguration.NO_OF_PLAYERS; i++) {
            showPlayerHighlightMarker(i, false);
        }
    }

    public void showTokenHighlightMarker(int playerId, int tokenId, boolean show) {
        players[playerId].showTokenHighlightMarker(tokenId, show);
    }

    public void hideAllTokenHighlightMarker(int playerId) {
        players[playerId].hideAllTokenHighlightMarker();
    }
}
