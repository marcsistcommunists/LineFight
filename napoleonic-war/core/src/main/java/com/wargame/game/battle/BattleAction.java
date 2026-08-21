package com.wargame.game.battle;

import com.wargame.game.entities.Regiment;

/**
 * Типы действий в бою
 */
enum ActionType {
    MOVE,   // Перемещение
    ATTACK, // Атака
    WAIT    // Ожидание (конец хода)
}

/**
 * Действие в бою
 */
public class BattleAction {
    
    public ActionType type;
    public int fromX, fromY;  // Откуда
    public int toX, toY;      // Куда (для MOVE - позиция, для ATTACK - цель)
    
    public BattleAction(ActionType type, int fromX, int fromY, int toX, int toY) {
        this.type = type;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }
    
    public static BattleAction move(int fromX, int fromY, int toX, int toY) {
        return new BattleAction(ActionType.MOVE, fromX, fromY, toX, toY);
    }
    
    public static BattleAction attack(int fromX, int fromY, int targetX, int targetY) {
        return new BattleAction(ActionType.ATTACK, fromX, fromY, targetX, targetY);
    }
    
    public static BattleAction wait(int x, int y) {
        return new BattleAction(ActionType.WAIT, x, y, x, y);
    }
}
