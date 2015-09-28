package com.logicanvas.games.boardgames.gametemplate.core;

import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesBasicCore;
import playn.core.Platform;

public class GameTemplate extends BoardGamesBasicCore {

  public GameTemplate (Platform plat) {
    super(plat, 33, 4, true); // update our "simulation" 33ms (30 times per second)
    initMenu(null);
  }

  @Override
  public void startGame() {

  }

  @Override
  public void closeGame() {

  }
}
