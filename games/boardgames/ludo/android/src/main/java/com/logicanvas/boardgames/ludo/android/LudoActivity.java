package com.logicanvas.boardgames.ludo.android;

import android.content.pm.ActivityInfo;
import playn.android.GameActivity;

import com.logicanvas.boardgames.ludo.core.Ludo;

public class LudoActivity extends GameActivity {

    @Override public void main () {
        new Ludo(platform());
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
