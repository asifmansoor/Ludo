package com.logicanvas.boardgames.ludo.menu.Buttons;

import com.logicanvas.boardgames.ludo.utility.CallBack;
import playn.scene.GroupLayer;

/**
 * Created by amansoor on 16-09-2015.
 */
public abstract class BasicButton extends GroupLayer{
    protected CallBack callBackImpl;

    public BasicButton() {
    }

    public BasicButton(CallBack callBackImpl) {
        this.callBackImpl = callBackImpl;
    }
}
