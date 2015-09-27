package com.logicanvas.boardgames.ludo.view;

import com.logicanvas.boardgames.ludo.config.GameConfiguration;
import playn.core.*;
import playn.scene.*;

/**
 * Created by amansoor on 06-09-2015.
 */
public class LudoText extends GroupLayer{
    GroupLayer base;
    private String[] textLines;
    private Graphics graphics;
    private TextWrap textWrap;
    private TextFormat captionTextFormat;
    private TextFormat textFormat;
    private CanvasLayer oldHistoryTextLayer;
    private CanvasLayer currentHistoryTextLayer;
    private CanvasLayer messageTextLayer;
    private TextBlock oldHistoryTextBlock;
    private TextBlock currentHistoryTextBlock;
    private TextBlock messageTextBlock;

    private CanvasLayer[] playerCaptionLayer;
    private int nextSlot;

    public LudoText(Graphics graphics) {
        this.graphics = graphics;
        base = new GroupLayer();
        this.add(base);
        textWrap = new TextWrap(graphics.viewSize.width() - graphics.viewSize.height());
        textFormat = new TextFormat(new Font("Courier", Font.Style.PLAIN, 11));
        textLines = new String[4];
        for (int i = 0; i < 4; i++) {
            textLines[i] = "";
        }
        nextSlot = 0;

        captionTextFormat = new TextFormat(new Font("Courier", Font.Style.ITALIC, 12));
        playerCaptionLayer = new CanvasLayer[GameConfiguration.NO_OF_PLAYERS];

    }

    public void setupPlayerCaptions(String[] text) {
        setPlayerCaption(GameConfiguration.BLUE_PLAYER_ID, text[0], graphics.viewSize.width() - graphics.viewSize.height() + 0.05f * graphics.viewSize.height(), 0.01f * (graphics.viewSize.height()));
        setPlayerCaption(GameConfiguration.RED_PLAYER_ID, text[1], graphics.viewSize.width() - graphics.viewSize.height() + 0.65f * graphics.viewSize.height(), 0.01f * (graphics.viewSize.height()));
        setPlayerCaption(GameConfiguration.GREEN_PLAYER_ID, text[2], graphics.viewSize.width() - graphics.viewSize.height() + 0.65f * graphics.viewSize.height(), 0.61f * (graphics.viewSize.height()));
        setPlayerCaption(GameConfiguration.YELLOW_PLAYER_ID, text[3], graphics.viewSize.width() - graphics.viewSize.height() + 0.05f * graphics.viewSize.height(), 0.61f * (graphics.viewSize.height()));
    }

    private void setPlayerCaption(int playerId, String text, float x, float y) {
        TextLayout[] layout = graphics.layoutText(text,captionTextFormat,textWrap);
        TextBlock block = new TextBlock(layout);
        Canvas canvas = block.toCanvas(graphics, TextBlock.Align.CENTER, 0xFF000000);
        playerCaptionLayer[playerId] = new CanvasLayer(graphics, canvas);
        playerCaptionLayer[playerId].setTranslation(x, y);
        base.add(playerCaptionLayer[playerId]);
    }

    public void showMessage(String message) {
        clearMessageTexts();
        TextLayout[] textLayouts = graphics.layoutText(message, textFormat, textWrap);
        messageTextBlock = new TextBlock(textLayouts);
        Canvas textCanvas = messageTextBlock.toCanvas(graphics, TextBlock.Align.LEFT, 0xFF0000FF);
        messageTextLayer = new CanvasLayer(graphics, textCanvas);
        messageTextLayer.setTranslation(0,0.34f*graphics.viewSize.height());
        base.add(messageTextLayer);
    }

    public void addText(String text) {
        textLines[nextSlot] = text;
        drawOldHistoryText();
        drawCurrentHistoryText();
        updateSlot();
    }

    private String getConcatenatedString() {
        String finalText = "";
        int i = 0;
        int tracker = nextSlot;
        while (i < 3) {
            tracker++;
            if (tracker >= 4) {
                tracker = 0;
            }
            finalText += textLines[tracker];
            i++;
        }

        return finalText;
    }

    private void drawCurrentHistoryText() {
        clearCurrentHistoryText();
        TextLayout[] currentTextLayouts = graphics.layoutText(textLines[nextSlot], textFormat, textWrap);
        currentHistoryTextBlock = new TextBlock(currentTextLayouts);
        Canvas currentTextCanvas = currentHistoryTextBlock.toCanvas(graphics, TextBlock.Align.LEFT, 0xFFFF0000);
        currentHistoryTextLayer = new CanvasLayer(graphics, currentTextCanvas);
        currentHistoryTextLayer.setTranslation(0, 0.74f*graphics.viewSize.height());

        base.add(oldHistoryTextLayer);
        base.add(currentHistoryTextLayer);
    }

    private void drawOldHistoryText() {
        clearOldHistoryTexts();
        TextLayout[] oldTextLayouts = graphics.layoutText(getConcatenatedString(), textFormat, textWrap);
        oldHistoryTextBlock = new TextBlock(oldTextLayouts);
        Canvas oldTextCanvas = oldHistoryTextBlock.toCanvas(graphics, TextBlock.Align.LEFT, 0xFF000000);
        oldHistoryTextLayer = new CanvasLayer(graphics, oldTextCanvas);
        oldHistoryTextLayer.setTranslation(0, 0.44f*graphics.viewSize.height());

        base.add(oldHistoryTextLayer);
    }

    private void updateSlot() {
        nextSlot++;
        if (nextSlot >= 4) {
            nextSlot = 0;
        }
    }

    private void clearOldHistoryTexts() {
        if (!base.isEmpty() && oldHistoryTextLayer != null) {
            base.remove(oldHistoryTextLayer);
            oldHistoryTextBlock = null;
        }
    }

    private void clearCurrentHistoryText() {
        if (!base.isEmpty() && currentHistoryTextLayer != null) {
            base.remove(currentHistoryTextLayer);
            currentHistoryTextBlock = null;
        }
    }

    private void clearMessageTexts() {
        if (!base.isEmpty() && messageTextLayer != null) {
            base.remove(messageTextLayer);
            messageTextBlock = null;
        }
    }
}
