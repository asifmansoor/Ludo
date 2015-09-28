package com.logicanvas.frameworks.boardgamesgdk.core.ui.widgets.Buttons;

import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
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
