package com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons;

import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import playn.core.Image;
import playn.scene.ImageLayer;

/**
 * Created by amansoor on 16-09-2015.
 */
public class ThreeStateButton extends TwoStateButton {
    private ImageLayer buttonInActive;

    public ThreeStateButton(Image.Region active, Image.Region pressed, Image.Region inactive, float x, float y, float width, float height, CallBack callBackImpl) throws ButtonCreationException {
        super(active, pressed, x, y, width, height, callBackImpl);

        if (inactive == null) {
            throw new ButtonCreationException();
        }

        init(inactive, x, y, width, height);
    }

    private void init(Image.Region inactive, float x, float y, float width, float height) {
        buttonInActive = new ImageLayer(inactive);
        buttonInActive.setSize(width, height);
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

    public void activateButton() {
        hideButton();
        buttonActive.setVisible(true);
    }

    public void deactivateButton() {
        hideButton();
        buttonInActive.setVisible(true);
    }
}
