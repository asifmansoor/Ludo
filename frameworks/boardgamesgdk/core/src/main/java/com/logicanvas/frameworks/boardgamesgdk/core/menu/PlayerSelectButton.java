package com.logicanvas.frameworks.boardgamesgdk.core.menu;

import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons.ButtonCreationException;
import com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons.TwoStateButtonWithText;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import playn.core.Graphics;
import playn.core.Image;
import playn.core.TextFormat;
import playn.core.TextWrap;

/**
 * Created by amansoor on 16-09-2015.
 */
public class PlayerSelectButton extends TwoStateButtonWithText {
    private String[] buttonTexts;
    private int textIndex;

    public PlayerSelectButton(Image.Region active, Image.Region pressed, float x, float y, float width, float height,
                              Graphics gfx, TextFormat format, TextWrap wrap, String[] text,
                              CallBack callBackImpl) throws ButtonCreationException {
        super(active, pressed, x, y, width, height, gfx, format, wrap, text[0], callBackImpl);
        buttonTexts = text;
        textIndex = 0;
    }

    @Override
    public void pressButton() {
        super.pressButton();
        setText(buttonTexts[incrementTextIndex()]);
    }

    private int incrementTextIndex() {
        textIndex++;
        if (textIndex >= buttonTexts.length) {
            textIndex = 0;
        }
        return textIndex;
    }

    public int getTextIndex() {
        return textIndex;
    }

    public void resetButton() {
        textIndex = 0;
        setText(buttonTexts[textIndex]);
    }
}
