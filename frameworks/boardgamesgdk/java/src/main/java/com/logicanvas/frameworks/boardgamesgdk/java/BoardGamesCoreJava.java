package com.logicanvas.frameworks.boardgamesgdk.java;

import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesBasicCore;
import playn.java.LWJGLPlatform;

public class BoardGamesCoreJava {

  public static void main (String[] args) {
    LWJGLPlatform.Config config = new LWJGLPlatform.Config();
    // use config to customize the Java platform, if needed
    LWJGLPlatform plat = new LWJGLPlatform(config);
    new BoardGamesBasicCore(plat, 33, 4, false) {
      @Override
      public void startGame() {

      }

      @Override
      public void closeGame() {

      }
    };
    plat.start();
  }
}
