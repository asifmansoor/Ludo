package com.logicanvas.frameworks.boardgamesgdk.html;

import com.google.gwt.core.client.EntryPoint;
import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesBasicCore;
import playn.html.HtmlPlatform;

public class BoardGamesCoreHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("boardgamesgdk/");
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
