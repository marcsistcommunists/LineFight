package com.wargame.game.battle;

import com.wargame.game.entities.Regiment;

/**
 * Боевая единица - обертка над полком для тактического боя
 */
public class BattleUnit {
    
    private Regiment regiment;
    private int ownerId;  // 0 - игрок, 1 - враг
    private int x, y;     // Позиция на поле
    
    // Очки действия за ход
    private int actionPoints;
    private int maxActionPoints;
    
    public BattleUnit(Regiment regiment, int ownerId) {
        this.regiment = regiment;
        this.ownerId = ownerId;
        this.x = 0;
        this.y = 0;
        this.maxActionPoints = regiment.getMovement();
        this.actionPoints = maxActionPoints;
    }
    
    /**
     * Получить урон
     */
    public void takeDamage(int damage) {
        regiment.takeDamage(damage);
    }
    
    /**
     * Восстановить очки действия в начале хода
     */
    public void refreshActionPoints() {
        this.actionPoints = maxActionPoints;
    }
    
    /**
     * Потратить очки действия
     */
    public boolean spendActionPoints(int cost) {
        if (actionPoints >= cost) {
            actionPoints -= cost;
            return true;
        }
        return false;
    }
    
    // Геттеры
    public Regiment getRegiment() { return regiment; }
    public int getOwnerId() { return ownerId; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getActionPoints() { return actionPoints; }
    public int getMaxActionPoints() { return maxActionPoints; }
    
    // Сеттеры позиции
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
