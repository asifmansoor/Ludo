package com.logicanvas.boardgames.ludo.menu.Buttons;

import com.logicanvas.boardgames.ludo.core.Ludo;
import com.logicanvas.boardgames.ludo.utility.CallBack;
import playn.core.*;
import playn.scene.CanvasLayer;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;

/**
 * Created by amansoor on 16-09-2015.
 */
public class TwoStateButtonWithText extends TwoStateButton {
    private GroupLayer buttonText;
    private Graphics gfx;
    private TextFormat format;
    private TextWrap wrap;
    private float textPosX;
    private float textPosY;

    public TwoStateButtonWithText(Image.Region active, Image.Region pressed, float x, float y, float width, float height, Graphics gfx, TextFormat format, TextWrap wrap, String text, CallBack callBackImpl) throws ButtonCreationException {
        super(active, pressed, x, y, width, height, callBackImpl);
        textPosX = x + width / 2;
        textPosY = y + height / 2;

        buttonText = new GroupLayer();

        this.remove(buttonActive);
        this.remove(buttonPressed);

        this.addAt(buttonActive, x, y);
        initText(gfx, format, wrap, text);
        this.add(buttonText);
        this.addAt(buttonPressed, x, y);
    }

    private void initText(Graphics gfx, TextFormat format, TextWrap wrap, String text) {
        this.gfx = gfx;
        this.format = format;
        this.wrap = wrap;
        setText(text);
    }

    public boolean updateText(String text) {
        if (gfx != null && text != null) {
            setText(text);
            return true;
        } else {
            return false;
        }
    }

    protected void setText(String text) {
        TextLayout[] lines = gfx.layoutText(text, format, wrap);
        TextBlock textBlock = new TextBlock(lines);
        Canvas canvas = textBlock.toCanvas(gfx, TextBlock.Align.CENTER, 0xFF000000);
        buttonText.removeAll();
        CanvasLayer buttonTextLayer = new CanvasLayer(gfx, canvas);
        buttonText.addCenterAt(buttonTextLayer, textPosX, textPosY);
    }

    @Override
    public void showButton() {
        super.showButton();
        buttonText.setVisible(true);
    }

    @Override
    public void hideButton() {
        super.hideButton();
        buttonText.setVisible(false);
    }
}
