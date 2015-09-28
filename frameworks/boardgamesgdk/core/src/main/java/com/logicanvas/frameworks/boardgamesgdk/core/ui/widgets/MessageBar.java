package com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets;

import playn.core.*;
import playn.scene.CanvasLayer;
import playn.scene.GroupLayer;

/**
 * Created by amansoor on 06-09-2015.
 */
public class MessageBar extends GroupLayer{
    GroupLayer base;
    private Graphics graphics;
    private TextWrap textWrap;
    private TextFormat textFormat;
    private CanvasLayer messageTextLayer;
    private TextBlock messageTextBlock;
    private int color;
    private float x, y;

    public MessageBar(Graphics graphics, float width, float x, float y) {
        this.graphics = graphics;
        base = new GroupLayer();
        this.add(base);
        textWrap = new TextWrap(width);
        textFormat = new TextFormat(new Font("Courier", Font.Style.PLAIN, 11));
        color = 0xFF0000FF;
        this.x = x;
        this.y = y;
    }

    public MessageBar(Graphics graphics, float width, float x, float y, int color, TextFormat format) {
        this.graphics = graphics;
        base = new GroupLayer();
        this.add(base);
        textWrap = new TextWrap(width);
        textFormat = format;
        this.color = color;
        this.x = x;
        this.y = y;
    }


    public void showMessage(String message) {
        clearMessageTexts();
        TextLayout[] textLayouts = graphics.layoutText(message, textFormat, textWrap);
        messageTextBlock = new TextBlock(textLayouts);
        Canvas textCanvas = messageTextBlock.toCanvas(graphics, TextBlock.Align.LEFT, color);
        messageTextLayer = new CanvasLayer(graphics, textCanvas);
        messageTextLayer.setTranslation(x, y);
        base.add(messageTextLayer);
    }


    private void clearMessageTexts() {
        if (!base.isEmpty() && messageTextLayer != null) {
            base.remove(messageTextLayer);
            messageTextBlock = null;
        }
    }
}
