package com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons;

import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.GameTimer;
import playn.core.*;
import playn.scene.*;
import playn.scene.Pointer;

/**
 * Created by amansoor on 16-09-2015.
 */
public class TwoStateButton extends BasicButton {
    protected ImageLayer buttonActive;
    protected ImageLayer buttonPressed;
    protected boolean pressedState;

    public TwoStateButton(Image.Region active, Image.Region pressed, float x, float y, float width, float height, CallBack callBackImpl) throws ButtonCreationException {
        super(callBackImpl);
        if (active == null || pressed == null) {
            throw new ButtonCreationException();
        }

        init(active, pressed, x, y, width, height);
    }

    private void init(Image.Region active, Image.Region pressed, float x, float y, float width, float height) {
        this.setWidth(width);
        this.setHeight(height);

        buttonActive = new ImageLayer(active);
        buttonPressed = new ImageLayer(pressed);

        buttonActive.setSize(width, height);
        buttonPressed.setSize(width, height);

        buttonActive.events().connect(new Pointer.Listener() {
            @Override
            public void onEnd(Pointer.Interaction iact) {
                if (!pressedState && isButtonActive()) {
                    onPressed();
                }
            }
        });

/*
        buttonActive.events().connect(new Mouse.Listener() {
            @Override
            public void onButton(Mouse.ButtonEvent event, Mouse.Interaction iact) {
                if (!pressedState && isButtonActive()) {
                    if (event.down) {
                        onPressed();
                    }
                }
            }
        });
*/


        this.addAt(buttonActive, x, y);
        this.addAt(buttonPressed, x, y);
        buttonPressed.setVisible(false);
        pressedState = false;    }

    public boolean isVisible() {
        if (buttonActive.visible() || buttonPressed.visible()) {
            return true;
        } else {
            return false;
        }
    }

    public void showButton() {
        hideButton();
        buttonActive.setVisible(true);
    }

    protected void pressButton() {
        buttonPressed.setVisible(true);
        GameTimer.runAfter(200, new CallBack() {
            @Override
            public void call(Object data) {
                buttonPressed.setVisible(false);
                pressedState = false;
            }
        });
    }

    public void hideButton() {
        buttonActive.setVisible(false);
        buttonPressed.setVisible(false);
    }

    protected void onPressed() {
        pressedState = true;
        pressButton();
        if (callBackImpl != null) {
            callBackImpl.call(null);
        }
    }

    public boolean isButtonActive() {
        return buttonActive.visible();
    }
}
