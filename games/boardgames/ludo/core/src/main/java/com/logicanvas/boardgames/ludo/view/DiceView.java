package com.logicanvas.boardgames.ludo.view;

import com.logicanvas.boardgames.ludo.config.GameConfiguration;
import com.logicanvas.boardgames.ludo.model.GameData;
import com.logicanvas.boardgames.ludo.ui.animations.BaseAnimation;
import com.logicanvas.boardgames.ludo.utility.CallBack;
import com.logicanvas.boardgames.ludo.utility.GameTimer;
import com.logicanvas.boardgames.ludo.utility.LudoLogger;
import playn.core.Image;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Pointer;
import playn.scene.RootLayer;

/**
 * Created by amansoor on 06-09-2015.
 */
public class DiceView extends GroupLayer{
    private ImageLayer[] diceLayers;
    private int[] offsets = {0,40,80,120,160,200};
    private CallBack callBackOnDiceClick;
    private CallBack callBackOnDiceAnimEnd;
    private BaseAnimation diceAnim;
    private int outcome;

    public DiceView(Platform platform, float boardSize) {
        // setup variables
        Image diceImage = platform.assets().getImage("images/dice.png");
        diceLayers = new ImageLayer[6];
        float width = 0.08f*(platform.graphics().viewSize.height());
        GroupLayer diceLayer = new GroupLayer(width, width);
        for (int i = 0; i < 6; i++) {
            Image.Region diceRegion = diceImage.region(offsets[i], 0, 40,
                    40);
            diceLayers[i] = new ImageLayer(diceRegion);
            diceLayers[i].setSize(width, width);
            diceLayers[i].setTx(0);
            diceLayers[i].setTy(0);
            diceLayers[i].setVisible(false);
            diceLayers[i].events().connect(new Pointer.Listener() {
                @Override
                public void onEnd(Pointer.Interaction iact) {
                    if (callBackOnDiceClick != null) {
                        callBackOnDiceClick.call(null);
                    }
                }
            });
            diceLayer.add(diceLayers[i]);
        }

        float x = (platform.graphics().viewSize.width() - boardSize)*3/4 + boardSize - width/2;//0.04f*platform.graphics().viewSize.width();
        float y = 0.4f*platform.graphics().viewSize.height();
        diceLayer.setTranslation(x, y);

        this.add(diceLayer);

        // setup animations
        diceAnim = new BaseAnimation(diceImage, 6, 1, 6, 2100, new CallBack() {
            @Override
            public void call(Object data) {
                onDieAnimationEnd();
            }
        });
    }

    public void hideDice() {
        for (int i = 0; i < 6; i++) {
            diceLayers[i].setVisible(false);
        }
    }

    public void showDice(int value) {
        hideDice();
        if (value > 0 && value <= 6) {
            diceLayers[value - 1].setVisible(true);
        }
    }

    public void playDieAnimation(int outcome) {
        this.outcome = outcome;
        hideDice();
        GameTimer.runAfter(500, new CallBack() {
            @Override
            public void call(Object data) {
                onDieAnimationEnd();
            }
        });

//        diceAnim.play();
    }

    public void onDieAnimationEnd() {
        showDice(outcome);
        if (callBackOnDiceAnimEnd != null) {
            LudoLogger.log("DiceView: calling back");
            callBackOnDiceAnimEnd.call(null);
        }
    }

    public void registerDiceClickListener(CallBack callBackImpl) {
        this.callBackOnDiceClick = callBackImpl;
    }

    public void registerDiceAnimEndListener(CallBack callBackImpl) {
        this.callBackOnDiceAnimEnd = callBackImpl;
    }
}
