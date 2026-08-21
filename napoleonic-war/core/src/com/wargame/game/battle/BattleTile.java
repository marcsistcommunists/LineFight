package com.wargame.game.battle;

import com.wargame.game.entities.Regiment;
import com.wargame.game.world.Province;

/**
 * Тайл поля боя
 */
public class BattleTile {
    
    private int x, y;
    private Province.TerrainType terrainType;
    
    // Особенности местности
    private boolean obstacle;   // Препятствие (нельзя пройти)
    private boolean cover;      // Укрытие (бонус к защите)
    private boolean difficult;  // Труднопроходимая местность
    
    // Юнит на тайле
    private BattleUnit unit;
    
    public BattleTile(int x, int y, Province.TerrainType terrainType) {
        this.x = x;
        this.y = y;
        this.terrainType = terrainType;
        this.obstacle = false;
        this.cover = false;
        this.difficult = false;
        this.unit = null;
    }
    
    // Геттеры и сеттеры
    public int getX() { return x; }
    public int getY() { return y; }
    public Province.TerrainType getTerrainType() { return terrainType; }
    
    public boolean hasObstacle() { return obstacle; }
    public void setObstacle(boolean obstacle) { this.obstacle = obstacle; }
    
    public boolean hasCover() { return cover; }
    public void setCover(boolean cover) { this.cover = cover; }
    
    public boolean isDifficult() { return difficult; }
    public void setDifficult(boolean difficult) { this.difficult = difficult; }
    
    public BattleUnit getUnit() { return unit; }
    public void setUnit(BattleUnit unit) { this.unit = unit; }
    
    public boolean hasUnit() { return unit != null && unit.getRegiment().isAlive(); }
}
