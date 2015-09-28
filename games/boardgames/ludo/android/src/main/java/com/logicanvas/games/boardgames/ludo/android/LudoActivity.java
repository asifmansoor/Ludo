package com.logicanvas.games.boardgames.ludo.android;

import com.logicanvas.boardgames.ludo.core.Ludo;
import playn.android.GameActivity;

public class LudoActivity extends GameActivity {

    @Override public void main () {
        new Ludo(platform());
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
