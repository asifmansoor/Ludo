package com.logicanvas.frameworks.boardgamesgdk.java;

import playn.java.LWJGLPlatform;

import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesCore;

public class BoardGamesCoreJava {

  public static void main (String[] args) {
    LWJGLPlatform.Config config = new LWJGLPlatform.Config();
    // use config to customize the Java platform, if needed
    LWJGLPlatform plat = new LWJGLPlatform(config);
    new BoardGamesCore(plat);
    plat.start();
  }
}
