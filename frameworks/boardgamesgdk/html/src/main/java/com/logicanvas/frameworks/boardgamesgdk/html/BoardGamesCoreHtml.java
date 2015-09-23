package com.logicanvas.frameworks.boardgamesgdk.html;

import com.google.gwt.core.client.EntryPoint;
import playn.html.HtmlPlatform;
import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesCore;

public class BoardGamesCoreHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("boardgamesgdk/");
    new BoardGamesCore(plat);
    plat.start();
  }
}
