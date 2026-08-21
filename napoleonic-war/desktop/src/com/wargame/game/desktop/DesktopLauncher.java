package com.wargame.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.wargame.game.NapoleonicWarGame;

/**
 * Запуск игры для Desktop (Windows/Linux/Mac).
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Napoleonic Wars: Dynamic Campaign");
        config.setWindowedMode(1280, 720);
        config.useVsync(true);
        
        new Lwjgl3Application(new NapoleonicWarGame(), config);
    }
}
