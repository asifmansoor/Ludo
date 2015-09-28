package com.logicanvas.games.boardgames.ludo.html;

import com.google.gwt.core.client.EntryPoint;
import com.logicanvas.games.boardgames.ludo.core.Ludo;
import playn.html.HtmlPlatform;

public class LudoHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("ludo/");
    new Ludo(plat);
    plat.start();
  }
}
