package com.logicanvas.boardgames.ludo.audio;

import com.logicanvas.boardgames.ludo.config.GameConfiguration;
import playn.core.Platform;
import playn.core.Sound;

/**
 * Created by amansoor on 07-09-2015.
 */
public class LudoSounds {
    private Sound[] sounds;
    private final int NO_OF_SOUNDS = 4;

    public LudoSounds(Platform platform) {
        sounds = new Sound[NO_OF_SOUNDS];
        sounds[GameConfiguration.GAME_SOUNDS_TOKEN_MOVE] = platform.assets().getSound("sounds/click");
        sounds[GameConfiguration.GAME_SOUNDS_PLAYER_TOUCH] = platform.assets().getSound("sounds/metalhit");
        sounds[GameConfiguration.GAME_SOUNDS_DICE_ROLL] = platform.assets().getSound("sounds/dice");
        sounds[GameConfiguration.GAME_SOUNDS_INVALID_PICK] = platform.assets().getSound("sounds/invalid");
    }

    public void playSound(int soundId) {
        if (soundId >= 0 && soundId < NO_OF_SOUNDS) {
            sounds[soundId].play();
        } else {
            // incorrect sound id
        }
    }
}
