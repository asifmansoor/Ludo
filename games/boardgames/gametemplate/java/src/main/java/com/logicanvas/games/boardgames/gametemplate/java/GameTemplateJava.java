package com.logicanvas.games.boardgames.gametemplate.java;

import playn.java.LWJGLPlatform;

import com.logicanvas.games.boardgames.gametemplate.core.GameTemplate;

public class GameTemplateJava {

  public static void main (String[] args) {
    LWJGLPlatform.Config config = new LWJGLPlatform.Config();
    // use config to customize the Java platform, if needed
    LWJGLPlatform plat = new LWJGLPlatform(config);
    new GameTemplate(plat);
    plat.start();
  }
}
