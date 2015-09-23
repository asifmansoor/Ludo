package com.logicanvas.frameworks.boardgamesgdk.android;

import playn.android.GameActivity;

import com.logicanvas.frameworks.boardgamesgdk.core.BoardGamesCore;

public class BoardGamesCoreActivity extends GameActivity {

  @Override public void main () {
    new BoardGamesCore(platform());
  }
}
