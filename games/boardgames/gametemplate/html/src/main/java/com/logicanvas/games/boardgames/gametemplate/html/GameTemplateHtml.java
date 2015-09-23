package com.logicanvas.games.boardgames.gametemplate.html;

import com.google.gwt.core.client.EntryPoint;
import playn.html.HtmlPlatform;
import com.logicanvas.games.boardgames.gametemplate.core.GameTemplate;

public class GameTemplateHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("gametemplate/");
    new GameTemplate(plat);
    plat.start();
  }
}
