package com.logicanvas.frameworks.boardgamesgdk.core.animations;

import com.logicanvas.frameworks.boardgamesgdk.core.utility.CallBack;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.GameTimer;
import playn.core.Image;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;

/**
 * Created by amansoor on 18-09-2015.
 */
public class BaseAnimation extends GroupLayer{
    private int timeLength;
    private ImageLayer[] frames;
    private int currentFrameIndex;
    private int timerIndex;
    private int frameStartTime;
    private int rate;
    private CallBack onEnd;

    public BaseAnimation(Image image, int numberOfFrames, int row, int col, int time, float x, float y, float width,
                         float height, float originalWidth, float originalHeight, CallBack onEnd) {
        this.onEnd = onEnd;
        timeLength = time;
        frames = new ImageLayer[numberOfFrames];
        for (int i = 0; i < numberOfFrames; i++) {
            int xOffset = i%col;
            int yOffset = i/col;
            Image.Region frameRegion = image.region(xOffset*originalWidth/col, yOffset*originalHeight/row, originalHeight/row, originalWidth/col);
            frames[i] = new ImageLayer(frameRegion);
            frames[i].setVisible(false);
            frames[i].setSize(width, height);
            frames[i].setTx(x);
            frames[i].setTy(y);
            this.add(frames[i]);
        }

        currentFrameIndex = 0;
        frameStartTime = 0;
        rate = timeLength/frames.length;
//        frames[2].setVisible(true);
    }

    public void play() {
        currentFrameIndex = 0;
        frameStartTime = 0;
        frames[currentFrameIndex].setVisible(true);
        timerIndex = GameTimer.addAnimListener(new CallBack() {
            @Override
            public void call(Object data) {
                update((int) data);
            }
        });
    }

    public void stop() {
        GameTimer.removeAnimListener(timerIndex);
        if (onEnd != null && isAnimComplete()) {
            onEnd.call(null);
        }
    }

    public void pause() {
        //
    }

    public void resume() {
        //
    }

    private void update(float deltaTime) {
        if (!isAnimComplete()) {
            if (frameStartTime >= rate) {
                frameStartTime = 0;
                changeFrame();
            } else {
                frameStartTime += deltaTime;
            }
        } else {
            stop();
        }
    }

    private void changeFrame() {
        frames[currentFrameIndex].setVisible(false);
        currentFrameIndex++;
        frames[currentFrameIndex].setVisible(true);
    }

    private boolean isAnimComplete() {
        if (currentFrameIndex >= frames.length - 1) {
            return true;
        } else {
            return false;
        }
    }
}
