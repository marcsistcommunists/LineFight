package com.wargame.game.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Область на глобальной карте (город, регион)
 */
public class Province {
    
    public enum TerrainType {
        CITY,       // Город
        FIELD,      // Поле
        FOREST,     // Лес
        HILLS,      // Холмы
        RIVER       // Река
    }
    
    private int id;
    private String name;
    private TerrainType terrainType;
    private int ownerId;  // ID владельца (0 - ничейная)
    
    // Соседние области (связи)
    private List<Integer> connectedProvinceIds;
    
    // Армия, находящаяся в области
    private Army stationedArmy;
    
    public Province(int id, String name, TerrainType terrainType) {
        this.id = id;
        this.name = name;
        this.terrainType = terrainType;
        this.ownerId = 0;
        this.connectedProvinceIds = new ArrayList<>();
        this.stationedArmy = null;
    }
    
    /**
     * Добавить связь с другой областью
     */
    public void addConnection(int provinceId) {
        if (!connectedProvinceIds.contains(provinceId)) {
            connectedProvinceIds.add(provinceId);
        }
    }
    
    /**
     * Удалить связь с другой областью
     */
    public void removeConnection(int provinceId) {
        connectedProvinceIds.remove(Integer.valueOf(provinceId));
    }
    
    /**
     * Проверить, соединена ли область с другой
     */
    public boolean isConnectedTo(int provinceId) {
        return connectedProvinceIds.contains(provinceId);
    }
    
    /**
     * Разместить армию в области
     */
    public void stationArmy(Army army) {
        this.stationedArmy = army;
        if (army != null) {
            army.setCurrentLocation(this);
        }
    }
    
    /**
     * Убрать армию из области
     */
    public void removeArmy() {
        this.stationedArmy = null;
    }
    
    /**
     * Проверить, есть ли армия в области
     */
    public boolean hasArmy() {
        return stationedArmy != null && stationedArmy.hasActiveRegiments();
    }
    
    /**
     * Проверить, враждебна ли армия в области
     */
    public boolean hasEnemyArmy(int playerId) {
        // Пока упрощенно - если армия есть и это не игрок (playerId = 0 для всех)
        return stationedArmy != null && playerId == 0;
    }
    
    // Геттеры и сеттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TerrainType getTerrainType() { return terrainType; }
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public List<Integer> getConnectedProvinceIds() { return connectedProvinceIds; }
    public Army getStationedArmy() { return stationedArmy; }
}
