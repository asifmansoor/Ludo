package com.logicanvas.games.boardgames.gametemplate.core;

import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesCore;
import com.logicanvas.frameworks.boardgamesgdk.core.utility.Utility;
import playn.core.Platform;

public class GameTemplate extends BoardGamesCore {

  public GameTemplate (Platform plat) {
    super(plat); // update our "simulation" 33ms (30 times per second)

    int[] test = {0,1,2};
    plat.log().info("Test framework: "+ Utility.findInArray(test, 1));
  }
}
