package com.logicanvas.boardgames.ludo.utility;

import playn.core.Clock;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by amansoor on 16-09-2015.
 */
public class GameTimer {
    private static ArrayList<TimeListener> listOfTimerListeners = new ArrayList<>();
    private static ArrayList<TimeListener> listOfAnimTimerListeners = new ArrayList<>();
    private static int animListenerCount = -1;
    private static float tick;

    public static void update(Clock clock) {
        tick = clock.tick;
        if (!listOfTimerListeners.isEmpty()) {
            for (int i = listOfTimerListeners.size() - 1; i >= 0; i--) {
                TimeListener listener = (TimeListener) listOfTimerListeners.get(i);
                if (listener.startTime + listener.timeInMilliSeconds < clock.tick) {
                    listener.callBack.call(null);
                    if (listOfTimerListeners.contains(listener)) {
                        listOfTimerListeners.remove(i);
                    }
                }
            }
        }
        if (!listOfAnimTimerListeners.isEmpty()) {
            Iterator itr = listOfAnimTimerListeners.iterator();
            while (itr.hasNext()) {
                TimeListener listener = (TimeListener) itr.next();
                listener.callBack.call(clock.dt);
            }
        }
    }

    public static void removeAllListeners() {
        LudoLogger.log("remove all time listeners");
        listOfTimerListeners.clear();
        listOfAnimTimerListeners.clear();
    }

    public static void runAfter(float timeInMilliSeconds, CallBack callBack) {
        TimeListener listener = new TimeListener(tick, timeInMilliSeconds, callBack);
        listOfTimerListeners.add(listener);
    }

    public static int addAnimListener(CallBack callBack) {
        ++animListenerCount;
        TimeListener listener = new TimeListener(tick, callBack);
        listOfAnimTimerListeners.add(animListenerCount, listener);
        return animListenerCount;
    }

    public static void removeAnimListener(int index) {
        listOfAnimTimerListeners.remove(index);
    }

    private static class TimeListener {
        private float startTime;
        private float timeInMilliSeconds;
        private CallBack callBack;

        public TimeListener(float startTime, CallBack callBack) {
            this.startTime = startTime;
            this.callBack = callBack;
        }

        public TimeListener(float startTime, float timeInMilliSeconds, CallBack callBack) {
            this.startTime = startTime;
            this.timeInMilliSeconds = timeInMilliSeconds;
            this.callBack = callBack;
        }
    }
}
