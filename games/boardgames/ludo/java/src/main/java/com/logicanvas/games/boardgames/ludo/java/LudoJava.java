package com.logicanvas.games.boardgames.ludo.java;

import com.logicanvas.games.boardgames.ludo.core.Ludo;
import playn.java.LWJGLPlatform;

public class LudoJava {

    public static void main(String[] args) {
        int[][] res = {{640,480},{640, 400},{720,406}};
        LWJGLPlatform.Config config = new LWJGLPlatform.Config();
        // use config to customize the Java platform, if needed
        config.appName = "BRYG";
        int resId = 1;
        config.width = res[resId][0];
        config.height = res[resId][1];
        LWJGLPlatform plat = new LWJGLPlatform(config);
        new Ludo(plat);
        plat.start();
    }
}
