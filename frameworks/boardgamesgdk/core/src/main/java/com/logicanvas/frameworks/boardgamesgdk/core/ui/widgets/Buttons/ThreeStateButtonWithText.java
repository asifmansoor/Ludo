package com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons;

import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import playn.core.*;
import playn.scene.ImageLayer;

/**
 * Created by amansoor on 15-09-2015.
 */
public class ThreeStateButtonWithText extends TwoStateButtonWithText {

    private ImageLayer buttonInActive;

    public ThreeStateButtonWithText(Image.Region active, Image.Region pressed, Image.Region inactive, float x, float y,
                                    float width, float height, Graphics gfx, TextFormat format, TextWrap wrap,
                                    String text, CallBack callBackImpl) throws ButtonCreationException {
        super(active, pressed, x, y, width, height, gfx, format, wrap, text, callBackImpl);
        if (inactive == null) {
            throw new ButtonCreationException();
        }

        buttonInActive = new ImageLayer(inactive);

        this.addAt(buttonInActive, x, y);
    }

    @Override
    public boolean isVisible() {
        if (buttonInActive.visible() || super.visible()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hideButton() {
        super.hideButton();
        buttonInActive.setVisible(false);
    }

}
