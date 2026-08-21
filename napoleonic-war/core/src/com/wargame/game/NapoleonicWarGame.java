package com.wargame.game;

/**
 * Основной класс игры, общий для всех платформ.
 */
public class NapoleonicWarGame extends com.badlogic.gdx.Game {
    
    @Override
    public void create() {
        // Иницициализация игры
        System.out.println("Napoleonic War Game Started!");
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        // Очистка ресурсов
    }
}
