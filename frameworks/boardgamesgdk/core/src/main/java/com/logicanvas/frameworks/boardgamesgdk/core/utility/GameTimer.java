package com.logicanvas.frameworks.boardgamesgdk.core.utility;

import playn.core.Clock;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by amansoor on 16-09-2015.
 */
public class GameTimer {
    private static ArrayList<TimeListener> listOfTimerListeners = new ArrayList<>();
    private static ArrayList<TimeListener> listOfAnimTimerListeners = new ArrayList<>();
    private static int currentlistenerId = -1;
    private static int currentAnimListenerId = -1;
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
            for (int i = listOfAnimTimerListeners.size() - 1; i >= 0; i--) {
                TimeListener listener = (TimeListener) listOfAnimTimerListeners.get(i);
                listener.callBack.call(clock.dt);
            }
        }
    }

    public static void removeAllListeners() {
        BoardGamesLogger.log("remove all time listeners");
        listOfTimerListeners.clear();
        listOfAnimTimerListeners.clear();
    }

    public static void runAfter(float timeInMilliSeconds, CallBack callBack) {
        currentlistenerId++;
        TimeListener listener = new TimeListener(currentlistenerId, tick, timeInMilliSeconds, callBack);
        listOfTimerListeners.add(listener);
    }

    public static int addAnimListener(CallBack callBack) {
        currentAnimListenerId++;
        TimeListener listener = new TimeListener(currentAnimListenerId, tick, callBack);
        listOfAnimTimerListeners.add(listener);
        return currentAnimListenerId;
    }

    public static void removeAnimListener(int index) {
        if (!listOfAnimTimerListeners.isEmpty()) {
            for (int i = 0; i < listOfAnimTimerListeners.size(); i++) {
                TimeListener listener = (TimeListener) listOfAnimTimerListeners.get(i);
                if (listener.timerId == index) {
                    listOfAnimTimerListeners.remove(i);
                }
            }
        } else {
            BoardGamesLogger.error("No listener found to be removed");
        }
    }

    private static class TimeListener {
        private int timerId;
        private float startTime;
        private float timeInMilliSeconds;
        private CallBack callBack;

        public TimeListener(int id, float startTime, CallBack callBack) {
            timerId = id;
            this.startTime = startTime;
            this.callBack = callBack;
        }

        public TimeListener(int id, float startTime, float timeInMilliSeconds, CallBack callBack) {
            timerId = id;
            this.startTime = startTime;
            this.timeInMilliSeconds = timeInMilliSeconds;
            this.callBack = callBack;
        }
    }
}
