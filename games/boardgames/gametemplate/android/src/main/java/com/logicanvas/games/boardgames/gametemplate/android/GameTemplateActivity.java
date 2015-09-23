package com.logicanvas.games.boardgames.gametemplate.android;

import playn.android.GameActivity;

import com.logicanvas.games.boardgames.gametemplate.core.GameTemplate;

public class GameTemplateActivity extends GameActivity {

  @Override public void main () {
    new GameTemplate(platform());
  }
}
